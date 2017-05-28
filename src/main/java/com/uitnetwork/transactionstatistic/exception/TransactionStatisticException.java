package com.uitnetwork.transactionstatistic.exception;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class TransactionStatisticException extends RuntimeException {
    public TransactionStatisticException() {
    }

    public TransactionStatisticException(String message) {
        super(message);
    }

    public TransactionStatisticException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionStatisticException(Throwable cause) {
        super(cause);
    }

    public TransactionStatisticException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
