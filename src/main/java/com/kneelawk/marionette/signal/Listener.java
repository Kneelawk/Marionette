package com.kneelawk.marionette.signal;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Executor;

/**
 * Utility object to help with holding Runnables and Executors as well as handling the linked list situation.
 */
class Listener {
    static final Listener TOMBSTONE = new Listener(null, null);

    final Runnable runnable;
    final Executor executor;

    @Nullable
    Listener next;

    Listener(Runnable runnable, Executor executor) {
        this.runnable = runnable;
        this.executor = executor;
    }
}
