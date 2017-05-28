package com.uitnetwork.transactionstatistic;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;

import org.junit.Test;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class TransactionStatisticApplicationUnitTest {

    TransactionStatisticApplication transactionStatisticApplication = new TransactionStatisticApplication();

    @Test
    public void shouldReturnUtcClockBean() {
        Clock utcClock = transactionStatisticApplication.utcClock();
        assertThat(utcClock).isEqualTo(Clock.systemUTC());
    }

}
