package com.kneelawk.marionette;

import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.RMIUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class ExecutionUtils {
    public static void executeIn(CurrentThread thread, Runnable runnable) {
        CurrentThread original = RMIUtils.findOriginal(thread);

        if (!(original instanceof ExecutorService)) {
            throw new IllegalArgumentException("Attempted to run something on an expired or different-process thread.");
        }

        try {
            ((ExecutorService) original).submit(runnable).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static <C> void pollQueue(Supplier<C> callbackSupplier, CallbackConsumer<C> callbackConsumer) {
        C callback;
        while ((callback = callbackSupplier.get()) != null) {
            C currentCallback = callback;
            BlockingCurrentThreadExecutor currentThread = new BlockingCurrentThreadExecutor();
            Future<Void> callbackFuture = MarionetteExecutors.getCallbackExecutor().submit(() -> {
                try {
                    callbackConsumer.accept(currentCallback, currentThread);
                } finally {
                    currentThread.shutdown();
                }

                return null;
            });

            currentThread.run();

            try {
                callbackFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO: Build an actual exception hierarchy for this.
                throw new RuntimeException("Exception while polling queue: " + e.getMessage(), e);
            }
        }
    }
}
