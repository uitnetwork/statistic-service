package com.uitnetwork.transactionstatistic.model;

import static com.uitnetwork.transactionstatistic.model.TransactionStatistic.ALL_ZEROS;

import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class TransactionStatisticPerEpochSecondArray {

    private final AtomicReferenceArray<TransactionStatisticPerEpochSecond> atomicReferenceArray;

    public TransactionStatisticPerEpochSecondArray(int arraySize) {
        atomicReferenceArray = new AtomicReferenceArray<TransactionStatisticPerEpochSecond>(arraySize);
        this.initialize();
    }

    private void initialize() {
        for (int i = 0; i < atomicReferenceArray.length(); ++i) {
            TransactionStatisticPerEpochSecond transactionStatisticPerEpochSecond = new TransactionStatisticPerEpochSecond(0, ALL_ZEROS);
            this.atomicReferenceArray.set(i, transactionStatisticPerEpochSecond);
        }
    }

    public TransactionStatisticPerEpochSecond get(int index) {
        return atomicReferenceArray.get(index);
    }

    public void set(int index, TransactionStatisticPerEpochSecond transactionStatistic) {
        atomicReferenceArray.set(index, transactionStatistic);
    }

    public TransactionStatistic sumAllAboveOrEqualEpochSecond(long epochSecond) {
        TransactionStatistic transactionStatistic = ALL_ZEROS;
        for (int i = 0; i < atomicReferenceArray.length(); ++i) {
            TransactionStatisticPerEpochSecond transactionStatisticPerEpochSecond = this.atomicReferenceArray.get(i);
            if (transactionStatisticPerEpochSecond.getEpochSecond() >= epochSecond) {
                transactionStatistic = transactionStatistic.addTransactionStatistic(transactionStatisticPerEpochSecond.getTransactionStatistic());
            }
        }
        return transactionStatistic;
    }
}
