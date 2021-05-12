package com.example.kafka_challenge_3a.controller;

import com.example.kafka_challenge_3a.api.TransactionsApi;
import com.example.kafka_challenge_3a.models.Transaction;
import com.example.kafka_challenge_3a.models.TransactionPage;
import com.example.kafka_challenge_3a.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

@Controller
@RequiredArgsConstructor
@Log4j2
public class TransactionController implements TransactionsApi {


    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionPage> getTransactions(@NotNull @Valid Integer accountId, @NotNull @Valid Integer month, @Valid Integer offset, @Valid Integer limit) {
        try{
            CompletableFuture<TransactionPage> transactionPage = transactionService.getTransactionPage(accountId, month, offset, limit);
            return new ResponseEntity<>(transactionPage.get(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
