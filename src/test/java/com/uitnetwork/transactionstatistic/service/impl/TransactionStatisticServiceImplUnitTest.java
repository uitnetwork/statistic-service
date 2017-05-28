package com.uitnetwork.transactionstatistic.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.uitnetwork.transactionstatistic.model.Transaction;
import com.uitnetwork.transactionstatistic.model.TransactionStatistic;
import com.uitnetwork.transactionstatistic.service.DateTimeService;

/**
 * Created by ninhdoan on 5/28/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionStatisticServiceImplUnitTest {
    private static final double TEST_AMOUNT = 12.34;
    private static final long TEST_CURRENT_UTC_TIMESTAMP = 1478192204000l;
    private static final long TEST_STALE_MILLIS = TEST_CURRENT_UTC_TIMESTAMP - 61000;
    private static final Transaction TEST_STALE_TRANSACTION = new Transaction(TEST_AMOUNT, TEST_STALE_MILLIS);
    private static final Transaction TEST_CURRENT_TRANSACTION = new Transaction(TEST_AMOUNT, TEST_CURRENT_UTC_TIMESTAMP);

    // @formatter:off
    private static final List<Transaction> TEST_TRANSACTIONS_NOT_STALE = Arrays.asList(
            new Transaction(123.45, TEST_CURRENT_UTC_TIMESTAMP - 1000),
            new Transaction(543.21, TEST_CURRENT_UTC_TIMESTAMP - 2000),
            new Transaction(999.88, TEST_CURRENT_UTC_TIMESTAMP - 3000),
            new Transaction(12.34, TEST_CURRENT_UTC_TIMESTAMP - 4000),
            new Transaction(987.65, TEST_CURRENT_UTC_TIMESTAMP - 5000),
            new Transaction(654.78, TEST_CURRENT_UTC_TIMESTAMP - 6000),
            new Transaction(1000, TEST_CURRENT_UTC_TIMESTAMP - 7000)
    );
    // @formatter:on


    @Mock
    private DateTimeService dateTimeService;

    @InjectMocks
    private TransactionStatisticServiceImpl transactionStatisticService;

    @Before
    public void setupTestData() {
        when(dateTimeService.getCurrentUTCEpochMillis()).thenReturn(TEST_CURRENT_UTC_TIMESTAMP);
    }

    @Test
    public void returnFalseWhenProcessStaleTransaction() {
        boolean success = transactionStatisticService.processTransaction(TEST_STALE_TRANSACTION);

        assertThat(success).isFalse();
        verify(dateTimeService).getCurrentUTCEpochMillis();
    }

    @Test
    public void returnTrueWhenProcessNotStaleTransaction() {
        boolean success = transactionStatisticService.processTransaction(TEST_CURRENT_TRANSACTION);

        assertThat(success).isTrue();
        verify(dateTimeService).getCurrentUTCEpochMillis();
    }

    @Test
    public void returnAllZeroTransactionStatisticWhenThereIsNoTransactionsAdded() {
        TransactionStatistic summaryTransactionStatistic = transactionStatisticService.getSummaryTransactionStatistic();
        verify(dateTimeService).getCurrentUTCEpochMillis();
    }

    @Test
    public void returnCorrectTransactionStatisticAfterAddingMultipleTransactions() throws InterruptedException {
        double expectedSum = TEST_TRANSACTIONS_NOT_STALE.stream().mapToDouble(Transaction::getAmount).sum();
        double expectedMin = TEST_TRANSACTIONS_NOT_STALE.stream().mapToDouble(Transaction::getAmount).min().getAsDouble();
        double expectedMax = TEST_TRANSACTIONS_NOT_STALE.stream().mapToDouble(Transaction::getAmount).max().getAsDouble();
        long expectedCount = TEST_TRANSACTIONS_NOT_STALE.stream().mapToDouble(Transaction::getAmount).count();
        double expectedAvg = expectedSum / expectedCount;

        for (Transaction transaction : TEST_TRANSACTIONS_NOT_STALE) {
            transactionStatisticService.processTransaction(transaction);
        }

        // TODO: find better solution
        Thread.sleep(500);

        TransactionStatistic summaryTransactionStatistic = transactionStatisticService.getSummaryTransactionStatistic();

        assertThat(summaryTransactionStatistic.getSum()).isEqualTo(expectedSum);
        assertThat(summaryTransactionStatistic.getMin()).isEqualTo(expectedMin);
        assertThat(summaryTransactionStatistic.getMax()).isEqualTo(expectedMax);
        assertThat(summaryTransactionStatistic.getCount()).isEqualTo(expectedCount);
        assertThat(summaryTransactionStatistic.getAvg()).isEqualTo(expectedAvg);
    }


}
