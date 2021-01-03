package com.kneelawk.marionette.client;

import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.MarionetteExecutors;
import com.kneelawk.marionette.api.RMIRunnable;
import com.kneelawk.marionette.signal.OneShotSignal;
import com.kneelawk.marionette.signal.ReusableSignal;

public class ClientGlobalSignals {
    private static final OneShotSignal SPLASH_SCREEN_SIGNAL = new OneShotSignal();
    private static final ReusableSignal GAME_JOIN_SIGNAL = new ReusableSignal();

    public static void signalSplashScreen() {
        SPLASH_SCREEN_SIGNAL.signal();
    }

    public static void addSplashScreenCallback(RMIRunnable callback) {
        SPLASH_SCREEN_SIGNAL
                .addListener(ExecutionUtils.toRunnable(callback), MarionetteExecutors.getCallbackExecutor());
    }

    public static void signalGameJoin() {
        GAME_JOIN_SIGNAL.signal();
    }

    public static void addGameJoinCallback(RMIRunnable callback) {
        GAME_JOIN_SIGNAL
                .addListener(ExecutionUtils.toRunnable(callback), MarionetteExecutors.getCallbackExecutor());
    }
}
