package com.uitnetwork.transactionstatistic.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.Clock;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by ninhdoan on 5/28/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DateTimeServiceImplTest {
    private static final long TEST_MILLIS = 1478192204000l;

    @Mock
    private Clock mockUtcClock;

    @InjectMocks
    private DateTimeServiceImpl dateTimeService;

    @Test
    public void test() {
        when(mockUtcClock.millis()).thenReturn(TEST_MILLIS);

        long currentUTCEpochMillis = dateTimeService.getCurrentUTCEpochMillis();

        assertThat(currentUTCEpochMillis).isEqualTo(TEST_MILLIS);

    }
}
