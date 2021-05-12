package com.example.kafka_challenge_3a.service;

import com.example.kafka_challenge_3a.entity.TransactionEntity;
import com.example.kafka_challenge_3a.mapper.TransactionMapper;
import com.example.kafka_challenge_3a.models.Transaction;
import com.example.kafka_challenge_3a.models.TransactionPage;
import com.example.kafka_challenge_3a.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    //final private CurrencyUtilService currencyUtilService;

    private final TransactionRepository transactionRepository;

    @Async
    public CompletableFuture<TransactionPage> getTransactionPage(long clientAccountNumber, int month, Integer offset, Integer limit) {
        List<TransactionEntity> transactionEntities;
        List<Transaction> transactions = new ArrayList();;
        TransactionPage transactionPage = new TransactionPage();
        try{
            transactionEntities = transactionRepository.findByClientAccountNumber(clientAccountNumber, limit,offset,month);
        } catch (Exception e){
           // log.info();
            throw e;
        }

        for (TransactionEntity te : transactionEntities){
            transactions.add(TransactionMapper.INSTANCE.mapTo(te));
        }

        try{
            transactionPage.setTransactions(transactions);
            transactionPage.setOffset(new BigDecimal(offset + transactions.size()));
            transactionPage.setTransactionCount(new BigDecimal(transactions.size()));


//            if(!transactions.isEmpty()) {
//                String currency = getCurrency(transactions.get(0));
//                Map<String, BigDecimal> debitCredit = getDebitCredit(transactions);
//                Map<String, BigDecimal> exchangedDebitCredit = exchangeDebitCredit(debitCredit, currency);
//                transactionPage.setCredit(exchangedDebitCredit.get("credit"));
//                transactionPage.setDebit(exchangedDebitCredit.get("debit"));
//                transactionPage.setExchangeRate(currencyUtilService.getExchangeRate(currency));
//            }
        } catch (Exception e){
           // log.error(e.getMessage(),e.toString());
            throw e;
        }
        return CompletableFuture.completedFuture(transactionPage);
    }
}
