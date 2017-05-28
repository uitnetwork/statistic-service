package com.uitnetwork.transactionstatistic.service;

import com.uitnetwork.transactionstatistic.model.Transaction;
import com.uitnetwork.transactionstatistic.model.TransactionStatistic;

/**
 * Created by ninhdoan on 5/27/17.
 */
public interface TransactionStatisticService {

    boolean processTransaction(Transaction transaction);

    TransactionStatistic getSummaryTransactionStatistic();
}
