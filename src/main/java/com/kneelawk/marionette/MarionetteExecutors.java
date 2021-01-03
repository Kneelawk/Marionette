package com.kneelawk.marionette;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MarionetteExecutors {
    private static final AtomicInteger CALLBACK_EXECUTOR_NUMBER = new AtomicInteger(1);
    private static final ExecutorService CALLBACK_EXECUTOR = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("Marionette-Callback-Thread-" + CALLBACK_EXECUTOR_NUMBER.getAndIncrement());
        // Making these threads daemons is probably ok for now. If this becomes an issue, we can just refactor the
        // ThreadWatchUnbinder to shut down this executor when it notices all the other threads are gone.
        thread.setDaemon(true);
        return thread;
    });

    public static ExecutorService getCallbackExecutor() {
        return CALLBACK_EXECUTOR;
    }
}
