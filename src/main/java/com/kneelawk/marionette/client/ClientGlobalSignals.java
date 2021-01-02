package com.kneelawk.marionette.client;

import com.google.common.util.concurrent.SettableFuture;
import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.MarionetteExecutors;
import com.kneelawk.marionette.api.RMIRunnable;

public class ClientGlobalSignals {
    private static final SettableFuture<Void> SPLASH_SCREEN_FUTURE = SettableFuture.create();

    public static void signalSplashScreen() {
        SPLASH_SCREEN_FUTURE.set(null);
    }

    public static void setSplashScreenCallback(RMIRunnable callback) {
        SPLASH_SCREEN_FUTURE
                .addListener(ExecutionUtils.toRunnable(callback), MarionetteExecutors.getCallbackExecutor());
    }
}
