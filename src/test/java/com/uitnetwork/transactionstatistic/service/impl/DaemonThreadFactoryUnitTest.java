package com.uitnetwork.transactionstatistic.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Created by ninhdoan on 5/28/17.
 */
public class DaemonThreadFactoryUnitTest {

    @Test
    public void returnDaemonThread() {
        DaemonThreadFactory daemonThreadFactory = new DaemonThreadFactory();
        Thread daemonThread = daemonThreadFactory.newThread(this::doNothing);

        assertThat(daemonThread.isDaemon()).isTrue();
    }

    private void doNothing() {
    }

}
