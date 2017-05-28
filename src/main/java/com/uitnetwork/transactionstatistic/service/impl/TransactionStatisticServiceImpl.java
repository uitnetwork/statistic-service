package com.uitnetwork.transactionstatistic.service.impl;

import static com.uitnetwork.transactionstatistic.model.TransactionStatistic.ALL_ZEROS;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uitnetwork.transactionstatistic.exception.TransactionStatisticException;
import com.uitnetwork.transactionstatistic.model.Transaction;
import com.uitnetwork.transactionstatistic.model.TransactionStatistic;
import com.uitnetwork.transactionstatistic.model.TransactionStatisticPerEpochSecond;
import com.uitnetwork.transactionstatistic.model.TransactionStatisticPerEpochSecondArray;
import com.uitnetwork.transactionstatistic.service.DateTimeService;
import com.uitnetwork.transactionstatistic.service.TransactionStatisticService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ninhdoan on 5/27/17.
 */
@Slf4j
@Service
public class TransactionStatisticServiceImpl implements TransactionStatisticService {
    // TODO: expose to properties
    public static final int HARDCODE_NUMBER_OF_SECONDS_TO_KEEP_TRANSACTION_STATISTIC = 60;
    public static final int HARDCODE_NUMBER_OF_THREADS_TO_PROCESS_TRANSACTION = 4;

    public static final int ONE_SECOND_AS_MILLIS = 1000;

    private final int numberOfSecondsToKeepTransactionStatistic;
    private final int numberOfMillisToKeepTransactionStatistic;
    private final int numberOfThreadsToProcessTransaction;
    private final TransactionStatisticPerEpochSecondArray transactionStatisticPerEpochSecondArray;
    private final BlockingQueue<Transaction> transactionBlockingQueues[];

    @Autowired
    private DateTimeService dateTimeService;

    public TransactionStatisticServiceImpl() {
        numberOfSecondsToKeepTransactionStatistic = HARDCODE_NUMBER_OF_SECONDS_TO_KEEP_TRANSACTION_STATISTIC;
        numberOfMillisToKeepTransactionStatistic = HARDCODE_NUMBER_OF_SECONDS_TO_KEEP_TRANSACTION_STATISTIC * ONE_SECOND_AS_MILLIS;
        numberOfThreadsToProcessTransaction = HARDCODE_NUMBER_OF_THREADS_TO_PROCESS_TRANSACTION;
        transactionStatisticPerEpochSecondArray = new TransactionStatisticPerEpochSecondArray(numberOfSecondsToKeepTransactionStatistic);
        transactionBlockingQueues = new BlockingQueue[numberOfThreadsToProcessTransaction];

        initializeTransactionBlockingQueues();
        initializeThreadsToProcessNewItemInQueue();
    }

    private void initializeTransactionBlockingQueues() {
        for (int i = 0; i < transactionBlockingQueues.length; ++i) {
            transactionBlockingQueues[i] = new LinkedBlockingQueue<>();
        }
    }

    private void initializeThreadsToProcessNewItemInQueue() {
        ExecutorService executorService = newFixedThreadPool(numberOfThreadsToProcessTransaction, new DaemonThreadFactory());
        for (int i = 0; i < numberOfThreadsToProcessTransaction; ++i) {
            final int queueNum = i;
            executorService.submit(() -> this.listenToProcessTransactionInQueue(queueNum));
        }
    }

    private void listenToProcessTransactionInQueue(int queueNum) {
        log.info("Thread: {} is waiting to process Transaction in queue: {}", currentThread().getName(), queueNum);

        BlockingQueue<Transaction> blockingQueue = transactionBlockingQueues[queueNum];
        try {
            while (true) {
                Transaction transaction = blockingQueue.take();
                accumulateTransactionToTransactionStatisticPerEpochSecondArray(transaction);
            }
        } catch (InterruptedException e) {
            log.error("Got InterruptedException for Thread: {} when processing queueNum: {}", currentThread().getName(), queueNum);
            throw new TransactionStatisticException(
                    "Got InterruptedException for Thread " + currentThread().getName() + " when processing queueNum: " + queueNum);
        }
    }

    private void accumulateTransactionToTransactionStatisticPerEpochSecondArray(Transaction transaction) {
        long transactionEpochSecond = transaction.getTimestamp() / ONE_SECOND_AS_MILLIS;
        long modOfEpochSecondWithNumberOfSeconds = transactionEpochSecond % numberOfSecondsToKeepTransactionStatistic;
        int arrayIndex = (int) modOfEpochSecondWithNumberOfSeconds;
        accumulateTransactionToArrayAtIndex(transaction, transactionEpochSecond, arrayIndex);
    }

    private void accumulateTransactionToArrayAtIndex(Transaction transaction, long transactionEpochSecond, int arrayIndex) {
        TransactionStatisticPerEpochSecond transactionStatisticPerEpochSecond = transactionStatisticPerEpochSecondArray.get(arrayIndex);
        long currentEpochSecondInStatistic = transactionStatisticPerEpochSecond.getEpochSecond();

        if (transactionEpochSecond < currentEpochSecondInStatistic) {
            log.warn("Got stale transaction: {}", transaction);
            return;
        }

        TransactionStatistic currentTransactionStatisticOfThisEpochSecond = transactionStatisticPerEpochSecond.getTransactionStatistic();
        if (transactionEpochSecond > currentEpochSecondInStatistic) {
            // reset for new epochSecond
            currentTransactionStatisticOfThisEpochSecond = ALL_ZEROS;
        }
        TransactionStatistic newTransactionStatistic = currentTransactionStatisticOfThisEpochSecond.addTransaction(transaction);
        transactionStatisticPerEpochSecondArray.set(arrayIndex, new TransactionStatisticPerEpochSecond(transactionEpochSecond, newTransactionStatistic));
    }

    @Override
    public boolean processTransaction(Transaction transaction) {
        long currentUTCEpochMillis = dateTimeService.getCurrentUTCEpochMillis();
        long transactionTimestamp = transaction.getTimestamp();

        if (currentUTCEpochMillis - transactionTimestamp > numberOfMillisToKeepTransactionStatistic) {
            return false;
        }

        int indexOfQueueToProcessTransaction = getIndexOfQueueToProcessTransaction(transactionTimestamp);
        boolean added = transactionBlockingQueues[indexOfQueueToProcessTransaction].offer(transaction);

        if (!added) {
            // when can not add to queue with capacity Integer.MAX_VALUE, need a better solution
            log.error("Failed to add transaction: {} to the queue, consider a new algorithm!", transaction);
            throw new TransactionStatisticException("Failed to add transaction: " + transaction + " to the queue");
        }

        return true;
    }

    private int getIndexOfQueueToProcessTransaction(long transactionTimestamp) {
        long epochSecond = transactionTimestamp / ONE_SECOND_AS_MILLIS;
        long modOfEpochSecondWithNumberOfSeconds = epochSecond % numberOfSecondsToKeepTransactionStatistic;
        int indexOfQueue = (int) modOfEpochSecondWithNumberOfSeconds % numberOfThreadsToProcessTransaction;
        return indexOfQueue;
    }

    @Override
    public TransactionStatistic getSummaryTransactionStatistic() {
        long currentUTCEpochMillis = dateTimeService.getCurrentUTCEpochMillis();
        long currentUtcEpochSecond = currentUTCEpochMillis / ONE_SECOND_AS_MILLIS;
        long lastValidEpochSecond = currentUtcEpochSecond - numberOfSecondsToKeepTransactionStatistic;
        return transactionStatisticPerEpochSecondArray.sumAllAboveOrEqualEpochSecond(lastValidEpochSecond);
    }
}
