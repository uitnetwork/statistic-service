package com.uitnetwork.transactionstatistic.service.impl;

import java.time.Clock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uitnetwork.transactionstatistic.service.DateTimeService;

/**
 * Created by ninhdoan on 5/28/17.
 */
@Service
public class DateTimeServiceImpl implements DateTimeService {

    @Autowired
    private Clock utcClock;

    @Override
    public long getCurrentUTCEpochMillis() {
        return utcClock.millis();
    }
}
