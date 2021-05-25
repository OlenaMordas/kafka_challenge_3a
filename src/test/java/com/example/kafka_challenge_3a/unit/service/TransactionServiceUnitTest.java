package com.example.kafka_challenge_3a.unit.service;

import com.example.kafka_challenge_3a.entity.TransactionEntity;
import com.example.kafka_challenge_3a.mapper.TransactionMapper;
import com.example.kafka_challenge_3a.models.Transaction;
import com.example.kafka_challenge_3a.models.TransactionPage;
import com.example.kafka_challenge_3a.repository.TransactionRepository;
import com.example.kafka_challenge_3a.service.TransactionService;
import com.example.kafka_challenge_3a.utils.CurrencyUtilService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceUnitTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CurrencyUtilService currencyUtilService;

    @InjectMocks
    private TransactionService transactionService;


    @Test
    public void getTransactionPage() throws Exception {

        //given
        Long accountId=new Long(31926819);
        int month = 5;
        int offset = 0;
        int limit=10;
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
        DecimalFormat df2 = new DecimalFormat("#");
        df2.setMaximumFractionDigits(2);
        transactionPage.setDebit(new BigDecimal(df2.format(0)));
        transactionPage.setDebitExchanged(new BigDecimal(df2.format(0)));
        transactionPage.setCredit(new BigDecimal(df2.format(9999.89)));
        transactionPage.setCreditExchanged(new BigDecimal(df2.format(9999.89)));
        transactionPage.setExchangeRate(new BigDecimal(df2.format(1)));

        // when
        when(transactionRepository.findByClientAccountNumber(accountId,10,0,5))
                .thenReturn(transactionEntities);
        when(currencyUtilService.getExchangeRate(Mockito.anyString()))
                .thenReturn(new Double(1));
        TransactionPage transactionPageRes = transactionService.getTransactionPage(accountId,month,offset,limit);

        //then
        assertEquals(transactionPage.getTransactionCount(),transactionPageRes.getTransactionCount());
        assertEquals(transactionPage.getCredit(),transactionPageRes.getCredit());
        assertEquals(transactionPage.getDebit(),transactionPageRes.getDebit());
        assertEquals(transactionPage.getExchangeRate(),transactionPageRes.getExchangeRate());
        assertEquals(transactionPage.getTransactions().get(0).getAccountId(), transactionPageRes.getTransactions().get(0).getAccountId());

    }
}