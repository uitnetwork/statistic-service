package com.uitnetwork.transactionstatistic.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.uitnetwork.transactionstatistic.model.Transaction;
import com.uitnetwork.transactionstatistic.model.TransactionStatistic;
import com.uitnetwork.transactionstatistic.service.TransactionStatisticService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ninhdoan on 5/27/17.
 */
@RestController
@Slf4j
public class TransactionStatisticRestController {

    @Autowired
    private TransactionStatisticService transactionStatisticService;

    @PostMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> processTransaction(@Valid @RequestBody Transaction transaction) {
        log.debug("Processing new Transaction: {}", transaction);
        boolean success = transactionStatisticService.processTransaction(transaction);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionStatistic getSummaryTransactionStatistic() {
        TransactionStatistic summaryTransactionStatistic = transactionStatisticService.getSummaryTransactionStatistic();
        return summaryTransactionStatistic;
    }
}
