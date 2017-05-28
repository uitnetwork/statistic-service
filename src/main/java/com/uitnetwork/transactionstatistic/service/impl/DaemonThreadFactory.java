package com.uitnetwork.transactionstatistic.service.impl;

import static java.util.concurrent.Executors.defaultThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        Thread t = defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    }
}
