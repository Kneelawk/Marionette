package com.kneelawk.marionette;

import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.RMIRunnable;
import com.kneelawk.marionette.api.RMIUtils;

import java.rmi.RemoteException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ExecutionUtils {
    public static void executeIn(CurrentThread thread, Runnable runnable) {
        CurrentThread original = RMIUtils.findOriginal(thread);

        if (!(original instanceof ExecutorService)) {
            throw new IllegalArgumentException("Attempted to run something on an expired or different-process thread.");
        }

        try {
            ((ExecutorService) original).submit(runnable).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO: Build an actual exception hierarchy for this.
            throw new RuntimeException("Exception while executing in existing thread: " + e.getMessage(), e);
        }
    }

    public static <T> T executeIn(CurrentThread thread, Callable<T> callable) {
        CurrentThread original = RMIUtils.findOriginal(thread);

        if (!(original instanceof ExecutorService)) {
            throw new IllegalArgumentException("Attempted to run something on an expired or different-process thread.");
        }

        try {
            return ((ExecutorService) original).submit(callable).get();
        } catch (InterruptedException e) {
            // TODO: Figure out what to do with this exception.
            throw new RuntimeException("Execution in thread interrupted", e);
        } catch (ExecutionException e) {
            // TODO: Build an actual exception hierarchy for this.
            throw new RuntimeException("Exception while executing in existing thread: " + e.getMessage(), e);
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

    public static void signalRMIRunnable(AtomicReference<RMIRunnable> reference) {
        RMIRunnable callback = reference.get();
        if (callback != null) {
            try {
                callback.run();
            } catch (RemoteException e) {
                // TODO: Handle this RemoteException properly.
                e.printStackTrace();
            }
        }
    }

    public static Runnable toRunnable(RMIRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (RemoteException e) {
                // TODO: Handle this RemoteException properly.
                e.printStackTrace();
            }
        };
    }
}
