package com.example.kafka_challenge_3a.intg.controller;

import com.example.kafka_challenge_3a.api.TransactionsApi;
import com.example.kafka_challenge_3a.models.TransactionPage;
import com.example.kafka_challenge_3a.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequiredArgsConstructor
public class TransactionController implements TransactionsApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionPage> getTransactions(@NotNull @Valid Integer accountId, @NotNull @Valid Integer month, @Valid Integer offset, @Valid Integer limit) {
        try{
            TransactionPage transactionPage = transactionService.getTransactionPage(accountId, month, offset, limit);
            return new ResponseEntity<TransactionPage>(transactionPage, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
