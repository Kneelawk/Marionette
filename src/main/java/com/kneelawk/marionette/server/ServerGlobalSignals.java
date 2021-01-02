package com.kneelawk.marionette.server;

import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.api.RMIRunnable;

import java.util.concurrent.atomic.AtomicReference;

public class ServerGlobalSignals {
    private static final AtomicReference<RMIRunnable> SERVER_STARTED_CALLBACK = new AtomicReference<>();

    public static void signalServerStarted() {
        ExecutionUtils.signalRMIRunnable(SERVER_STARTED_CALLBACK);
    }

    public static void setServerStartedCallback(RMIRunnable callback) {
        SERVER_STARTED_CALLBACK.set(callback);
    }
}
