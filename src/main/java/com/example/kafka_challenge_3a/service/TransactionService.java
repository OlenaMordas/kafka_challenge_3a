package com.example.kafka_challenge_3a.service;

import com.example.kafka_challenge_3a.entity.TransactionEntity;
import com.example.kafka_challenge_3a.mapper.TransactionMapper;
import com.example.kafka_challenge_3a.models.Transaction;
import com.example.kafka_challenge_3a.models.TransactionPage;
import com.example.kafka_challenge_3a.repository.TransactionRepository;
import com.example.kafka_challenge_3a.utils.CurrencyUtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

    private final CurrencyUtilService currencyUtilService;

    private final TransactionRepository transactionRepository;

    @Async
    public CompletableFuture<TransactionPage> getTransactionPage(long clientAccountNumber, int month, Integer offset, Integer limit) throws Exception {
        List<TransactionEntity> transactionEntities;
        List<Transaction> transactions = new ArrayList();

        TransactionPage transactionPage = new TransactionPage();
        try {
            transactionEntities = transactionRepository.findByClientAccountNumber(clientAccountNumber, limit, offset, month);
        } catch (Exception e) {
            throw new Exception(String.format("There are no values of transactions for the given account number. Response: %s", e.toString()));
        }

        for (TransactionEntity te : transactionEntities) {
            transactions.add(TransactionMapper.INSTANCE.mapTo(te));
        }

        try {
            transactionPage.setTransactions(transactions);
            transactionPage.setOffset(new BigDecimal(offset + transactions.size()));
            transactionPage.setTransactionCount(new BigDecimal(transactions.size()));

            if (!transactions.isEmpty()) {
                String currency = getCurrency(transactions.get(0));
                Map<String, BigDecimal> debitCredit = getDebitCredit(transactions);
                transactionPage.setCredit(debitCredit.get("credit"));
                transactionPage.setDebit(debitCredit.get("debit"));
                setExchangeDebitCredit(transactionPage, currency);
            }
        } catch (Exception e) {
            throw new Exception(String.format("Error setting Transaction Page properties: %s", e.toString()));
        }
        return CompletableFuture.completedFuture(transactionPage);
    }

    private Map<String, BigDecimal> getDebitCredit(List<Transaction> transactions) {
        double creditSum = 0;
        double debitSum = 0;

        for (Transaction transaction : transactions) {
            String amount = transaction.getAmount().replaceAll("\\s", "").substring(3);
            if (amount.charAt(0) == '-') {
                debitSum += Double.parseDouble(amount);
            } else {
                creditSum += Double.parseDouble(amount);
            }
        }

        Map<String, BigDecimal> res = new HashMap<>();
        DecimalFormat df2 = new DecimalFormat("#");
        df2.setMaximumFractionDigits(2);
        res.put("credit", new BigDecimal(df2.format(creditSum)));
        res.put("debit", new BigDecimal(df2.format(debitSum)));
        return res;
    }

    private void setExchangeDebitCredit(TransactionPage transactionPage, String currency) throws Exception {
        DecimalFormat df2 = new DecimalFormat("#");
        df2.setMaximumFractionDigits(2);
        BigDecimal debit = transactionPage.getDebit();
        BigDecimal credit = transactionPage.getCredit();
        double rate = currency.equals("EUR") ? 1 : currencyUtilService.getExchangeRate(currency);
        BigDecimal debitExchanged = new BigDecimal(df2.format(debit.doubleValue() / rate));
        BigDecimal creditExchanged = new BigDecimal(df2.format(credit.doubleValue() / rate));
        transactionPage.setExchangeRate(new BigDecimal(df2.format(rate)));
        transactionPage.setDebitExchanged(debitExchanged);
        transactionPage.setCreditExchanged(creditExchanged);
    }

    private String getCurrency(Transaction transaction) {
        return transaction.getAmount().replaceAll(" ", "").substring(0, 3);
    }
}
