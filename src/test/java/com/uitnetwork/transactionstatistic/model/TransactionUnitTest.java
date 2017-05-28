package com.uitnetwork.transactionstatistic.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ClassUtils;

/**
 * Created by ninhdoan on 5/27/17.
 */
public class TransactionUnitTest {

    private static final double TEST_AMOUNT = 123.45;
    private static final long TEST_TIMESTAMP = 1478192204000l;

    @Test
    public void shouldHasPublicNoArgsConstructor() {
        boolean hasPublicNoArgsConstructor = ClassUtils.hasConstructor(Transaction.class);

        assertThat(hasPublicNoArgsConstructor).isTrue();
    }

    @Test
    public void shouldHasGetterSetter() {
        Transaction transaction = new Transaction();
        transaction.setAmount(TEST_AMOUNT);
        transaction.setTimestamp(TEST_TIMESTAMP);

        assertThat(transaction.getAmount()).isEqualTo(TEST_AMOUNT);
        assertThat(transaction.getTimestamp()).isEqualTo(TEST_TIMESTAMP);
    }
}
