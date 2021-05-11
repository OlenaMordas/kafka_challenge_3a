package com.example.kafka_challenge_3a.controller;

import com.example.kafka_challenge_3a.api.TransactionsApi;
import com.example.kafka_challenge_3a.models.Transaction;
import com.example.kafka_challenge_3a.models.TransactionPage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;

@RestController
public class TransactionController implements TransactionsApi {

    @Override
    public ResponseEntity<TransactionPage> getTransactions(@NotNull @Valid Integer accountId, @NotNull @Valid Integer month, @Valid Integer offset, @Valid Integer limit) {
        Transaction t1 = new Transaction();
        t1.setId("test");
        t1.setAccountId(new BigDecimal(10000));
        t1.setAmount("USD 100");
        t1.setIban("test");
        t1.setDesription("test");
        t1.setValueDate("test");

        TransactionPage tp = new TransactionPage();
        tp.setTransactions(Arrays.asList(t1));

        return new ResponseEntity<TransactionPage>(tp, HttpStatus.OK );
    }
}
