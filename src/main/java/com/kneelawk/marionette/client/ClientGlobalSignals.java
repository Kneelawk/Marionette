package com.kneelawk.marionette.client;

import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.api.RMIRunnable;

import java.util.concurrent.atomic.AtomicReference;

public class ClientGlobalSignals {
    private static final AtomicReference<RMIRunnable> SPLASH_SCREEN_CALLBACK = new AtomicReference<>();

    public static void signalSplashScreen() {
        ExecutionUtils.signalRMIRunnable(SPLASH_SCREEN_CALLBACK);
    }

    public static void setSplashScreenCallback(RMIRunnable callback) {
        SPLASH_SCREEN_CALLBACK.set(callback);
    }
}
