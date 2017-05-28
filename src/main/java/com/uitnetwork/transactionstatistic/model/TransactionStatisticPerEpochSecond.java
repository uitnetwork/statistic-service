package com.uitnetwork.transactionstatistic.model;

import lombok.Getter;

/**
 * Created by ninhdoan on 5/28/17.
 */
@Getter
public class TransactionStatisticPerEpochSecond {
    private final long epochSecond;

    private final TransactionStatistic transactionStatistic;

    public TransactionStatisticPerEpochSecond(long epochSecond, TransactionStatistic transactionStatistic) {
        this.epochSecond = epochSecond;
        this.transactionStatistic = transactionStatistic;
    }
}
