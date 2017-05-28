package com.uitnetwork.transactionstatistic.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class GlobalTransactionStatisticExceptionHandlerUnitTest {

    @Test
    public void shouldHaveControllerAdviceAnnotation() {
        ControllerAdvice controllerAdviceAnnotation = findAnnotation(GlobalTransactionStatisticExceptionHandler.class, ControllerAdvice.class);

        assertThat(controllerAdviceAnnotation).isNotNull();
    }

    @Test
    public void shouldHaveExceptionHandlerAndResponseStatusAnnotation() throws NoSuchMethodException {
        Method handleTransactionStatisticExceptionMethod =
                GlobalTransactionStatisticExceptionHandler.class.getMethod("handleTransactionStatisticException", TransactionStatisticException.class);

        ExceptionHandler exceptionHandlerAnnotation = findAnnotation(handleTransactionStatisticExceptionMethod, ExceptionHandler.class);
        ResponseStatus responseStatusAnnotation = findAnnotation(handleTransactionStatisticExceptionMethod, ResponseStatus.class);

        assertThat(exceptionHandlerAnnotation).isNotNull();
        assertThat(responseStatusAnnotation).isNotNull();
        assertThat(exceptionHandlerAnnotation.value()).contains(TransactionStatisticException.class);
        assertThat(responseStatusAnnotation.code()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
