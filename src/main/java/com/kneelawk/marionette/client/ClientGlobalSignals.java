package com.kneelawk.marionette.client;

import com.kneelawk.marionette.api.RMIRunnable;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicReference;

public class ClientGlobalSignals {
    private static final AtomicReference<RMIRunnable> SPLASH_SCREEN_CALLBACK = new AtomicReference<>();

    public static void signalSplashScreen() {
        RMIRunnable callback = SPLASH_SCREEN_CALLBACK.get();
        if (callback != null) {
            try {
                callback.run();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setSplashScreenCallback(RMIRunnable callback) {
        SPLASH_SCREEN_CALLBACK.set(callback);
    }
}
