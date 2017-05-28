package com.uitnetwork.transactionstatistic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ninhdoan on 5/28/17.
 */
@Slf4j
@ControllerAdvice
public class GlobalTransactionStatisticExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(TransactionStatisticException.class)
    public void handleTransactionStatisticException(TransactionStatisticException transactionStatisticException) {
        log.error("handleTransactionStatisticException()", transactionStatisticException);
    }
}
