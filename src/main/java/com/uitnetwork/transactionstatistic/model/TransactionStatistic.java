package com.uitnetwork.transactionstatistic.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by ninhdoan on 5/28/17.
 */
@Getter
@EqualsAndHashCode
public class TransactionStatistic {
    public static final TransactionStatistic ALL_ZEROS = new TransactionStatistic();

    private final double sum;

    private final double avg;

    private final double max;

    private final double min;

    private final long count;

    private TransactionStatistic() {
        this(0, 0, 0, 0, 0);
    }

    private TransactionStatistic(double sum, double avg, double max, double min, long count) {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public TransactionStatistic addTransaction(Transaction transaction) {
        double transactionAmount = transaction.getAmount();
        double newSum = this.sum + transactionAmount;
        double newMax = this.count == 0 || this.max < transactionAmount ? transactionAmount : this.max;
        double newMin = this.count == 0 || this.min > transactionAmount ? transactionAmount : this.min;
        long newCount = this.count + 1;
        double newAvg = newSum / newCount;
        return new TransactionStatistic(newSum, newAvg, newMax, newMin, newCount);
    }

    public TransactionStatistic addTransactionStatistic(TransactionStatistic transactionStatistic) {
        if (this.count == 0) {
            return transactionStatistic;
        }
        if (transactionStatistic.count == 0) {
            return this;
        }

        double newSum = this.sum + transactionStatistic.sum;
        double newMax = this.max < transactionStatistic.max ? transactionStatistic.max : this.max;
        double newMin = this.min > transactionStatistic.min ? transactionStatistic.min : this.min;
        long newCount = this.count + transactionStatistic.count;
        double newAvg = newSum / newCount;

        return new TransactionStatistic(newSum, newAvg, newMax, newMin, newCount);
    }
}
