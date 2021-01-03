package com.kneelawk.marionette.signal;

import java.util.concurrent.Executor;

public interface Signal {
    boolean signal();

    void addListener(Runnable runnable, Executor executor);
}
