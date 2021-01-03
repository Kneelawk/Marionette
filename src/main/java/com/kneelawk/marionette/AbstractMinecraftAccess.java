package com.kneelawk.marionette;

import java.util.concurrent.CountDownLatch;

public abstract class AbstractMinecraftAccess {
    private final CountDownLatch startLatch = new CountDownLatch(1);

    // WHY JAVA? Why do you let me do this?
    public void start() {
        startLatch.countDown();
    }

    public void awaitStart() throws InterruptedException {
        startLatch.await();
    }
}
