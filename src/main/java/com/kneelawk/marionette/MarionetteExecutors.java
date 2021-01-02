package com.kneelawk.marionette;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MarionetteExecutors {
    private static final ExecutorService CALLBACK_EXECUTOR = Executors.newCachedThreadPool();

    public static ExecutorService getCallbackExecutor() {
        return CALLBACK_EXECUTOR;
    }
}
