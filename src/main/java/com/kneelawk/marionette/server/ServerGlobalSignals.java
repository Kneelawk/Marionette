package com.kneelawk.marionette.server;

import com.google.common.util.concurrent.SettableFuture;
import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.MarionetteExecutors;
import com.kneelawk.marionette.api.RMIRunnable;

public class ServerGlobalSignals {
    private static final SettableFuture<Void> SERVER_STARTED_FUTURE = SettableFuture.create();

    public static void signalServerStarted() {
        SERVER_STARTED_FUTURE.set(null);
    }

    public static void setServerStartedCallback(RMIRunnable callback) {
        SERVER_STARTED_FUTURE
                .addListener(ExecutionUtils.toRunnable(callback), MarionetteExecutors.getCallbackExecutor());
    }
}
