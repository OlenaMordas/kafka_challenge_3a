package com.example.kafka_challenge_3a.intg.controller;

import com.example.kafka_challenge_3a.entity.TransactionEntity;
import com.example.kafka_challenge_3a.mapper.TransactionMapper;
import com.example.kafka_challenge_3a.models.Transaction;
import com.example.kafka_challenge_3a.models.TransactionPage;
import com.example.kafka_challenge_3a.repository.CurrencyRepository;
import com.example.kafka_challenge_3a.repository.TransactionRepository;
import com.example.kafka_challenge_3a.service.TransactionService;
import com.example.kafka_challenge_3a.utils.CurrencyUtilService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
class TransactionControllerIntgTest {

    @Autowired
    private MockMvc mvc;

    @SpyBean
    private TransactionService transactionService;

    @MockBean
    private TransactionRepository transactionRepository;

    @SpyBean
    private CurrencyUtilService currencyUtilService;

    @MockBean
    private CurrencyRepository currencyRepository;

    @Test
    void getTransactions() throws Exception {

        //given
        Long accountId=new Long(31926819);
        int month = 5;
        int offset = 0;
        int limit=100;
        TransactionEntity transactionEntity = new TransactionEntity(
                "0000",
                accountId,
                "EUR 9999.89",
                "GB22NWBK70011931926888",
                new Date(),
                "description");
        List<TransactionEntity> transactionEntities = Arrays.asList(transactionEntity);
        List<Transaction> transactions = new ArrayList();

        for (TransactionEntity te : transactionEntities) {
            transactions.add(TransactionMapper.INSTANCE.mapTo(te));
        }

        TransactionPage transactionPage = new TransactionPage();
        transactionPage.setTransactions(transactions);

        transactionPage.setOffset(new BigDecimal(1));
        transactionPage.setTransactionCount(new BigDecimal(1));
        transactionPage.setDebit(new BigDecimal(0));
        transactionPage.setDebitExchanged(new BigDecimal(0));
        transactionPage.setCredit(new BigDecimal(9999.89));
        transactionPage.setCreditExchanged(new BigDecimal(9999.89));
        transactionPage.setExchangeRate(new BigDecimal(1));


        //when
        doReturn(transactionEntities).when(transactionRepository).findByClientAccountNumber(accountId, limit, offset,month);
        //        when(transactionRepository.findByClientAccountNumber(accountId, limit, offset,month ))
        //                .thenReturn(transactionEntities);

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(3, TimeUnit.SECONDS);

        //then
        mvc.perform(get("/transactions")
                .param("accountId", String.valueOf(accountId))
                .param("month", "5")
                .param("offset","0")
                .param("limit","100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.debit", notNullValue()))
                .andExpect(jsonPath("$.debit").value("0"))
                .andExpect(jsonPath("$.credit").value("9999.89"))
                .andExpect(jsonPath("$.transactions.length()").value(transactions.size()));
    }
}