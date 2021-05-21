//package com.example.kafka_challenge_3a.intg.controller;
//
//import com.example.kafka_challenge_3a.entity.TransactionEntity;
//import com.example.kafka_challenge_3a.mapper.TransactionMapper;
//import com.example.kafka_challenge_3a.models.Transaction;
//import com.example.kafka_challenge_3a.models.TransactionPage;
//import com.example.kafka_challenge_3a.repository.TransactionRepository;
//import com.example.kafka_challenge_3a.service.TransactionService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//import static org.hamcrest.core.Is.isA;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
////@TestPropertySource("/application.properties")\
//@ActiveProfiles("test")
//@RunWith(SpringRunner.class)
//@WebMvcTest(TransactionController.class)
//class TransactionControllerIntgTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//
//    @MockBean
//    private TransactionService transactionService;
//
//    @MockBean
//    private TransactionRepository transactionRepository;
//
//    @BeforeEach
//    void setUp(){
//
//    }
//
//    @Test
//    void getTransactions() throws Exception {
//
//        //given
//        Long accountId=new Long(31926819);
//        int month = 5;
//        int offset = 0;
//        int limit=10;
//        TransactionEntity transactionEntity = new TransactionEntity(
//                "0000",
//                accountId,
//                "EUR 9999.89",
//                "GB22NWBK70011931926888",
//                new Date(),
//                "description");
//        List<TransactionEntity> transactionEntities = Arrays.asList(transactionEntity);
//        List<Transaction> transactions = new ArrayList();
//
//        for (TransactionEntity te : transactionEntities) {
//            transactions.add(TransactionMapper.INSTANCE.mapTo(te));
//        }
//
//        TransactionPage transactionPage = new TransactionPage();
//        transactionPage.setTransactions(transactions);
//
//        transactionPage.setOffset(new BigDecimal(1));
//        transactionPage.setTransactionCount(new BigDecimal(1));
//        transactionPage.setDebit(new BigDecimal(0));
//        transactionPage.setDebitExchanged(new BigDecimal(0));
//        transactionPage.setCredit(new BigDecimal(9999.89));
//        transactionPage.setCreditExchanged(new BigDecimal(9999.89));
//        transactionPage.setExchangeRate(new BigDecimal(1));
//
//
//        //when
//        when(transactionRepository.findByClientAccountNumber(accountId, limit, offset,month ))
//                .thenReturn(transactionEntities);
//        //then
//        mvc.perform(get("/transactions")
//                .param("accountId", String.valueOf(accountId))
//                .param("month", "5")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", isA(TransactionPage.class)));
//    }
//}