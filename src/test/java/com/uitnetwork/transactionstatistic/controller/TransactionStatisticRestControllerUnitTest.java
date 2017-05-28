package com.uitnetwork.transactionstatistic.controller;

import static com.uitnetwork.transactionstatistic.model.TransactionStatistic.ALL_ZEROS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.uitnetwork.transactionstatistic.model.Transaction;
import com.uitnetwork.transactionstatistic.model.TransactionStatistic;
import com.uitnetwork.transactionstatistic.service.TransactionStatisticService;

/**
 * Created by ninhdoan on 5/28/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionStatisticRestControllerUnitTest {

    private final Transaction testTransaction = new Transaction(100, 1478192204000l);

    private final TransactionStatistic testTransactionStatistic = ALL_ZEROS.addTransaction(testTransaction);

    @Mock
    private TransactionStatisticService transactionStatisticService;

    @InjectMocks
    private TransactionStatisticRestController transactionStatisticRestController;

    @Test
    public void returnEmptyBodyWith201WhenSuccessfullyProcessedNewTransaction() {
        when(transactionStatisticService.processTransaction(testTransaction)).thenReturn(true);

        ResponseEntity<Void> responseEntity = transactionStatisticRestController.processTransaction(testTransaction);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNull();
        verify(transactionStatisticService).processTransaction(testTransaction);
    }

    @Test
    public void returnEmptyBodyWith204WhenFailedProcessedNewTransaction() {
        when(transactionStatisticService.processTransaction(testTransaction)).thenReturn(false);

        ResponseEntity<Void> responseEntity = transactionStatisticRestController.processTransaction(testTransaction);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(responseEntity.getBody()).isNull();
        verify(transactionStatisticService).processTransaction(testTransaction);
    }

    @Test
    public void returnTransactionStatisticFromService() {
        when(transactionStatisticService.getSummaryTransactionStatistic()).thenReturn(testTransactionStatistic);

        TransactionStatistic summaryTransactionStatistic = transactionStatisticRestController.getSummaryTransactionStatistic();

        assertThat(testTransactionStatistic == summaryTransactionStatistic).isTrue();
    }
}
