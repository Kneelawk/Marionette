package com.kneelawk.marionette.signal;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This signal can only be called once. If listeners are added to this signal after it has already been sent, those
 * listeners will be invoked immediately.
 */
public class OneShotSignal implements Signal {
    private final AtomicReference<Listener> listeners = new AtomicReference<>(null);
    private final AtomicBoolean signaled = new AtomicBoolean(false);

    @Override
    public boolean signal() {
        if (!signaled.getAndSet(true)) {
            Listener head = listeners.getAndSet(Listener.TOMBSTONE);

            Listener reversedHead = null;
            while (head != null) {
                Listener tmp = head;
                head = head.next;
                tmp.next = reversedHead;
                reversedHead = tmp;
            }

            while (reversedHead != null) {
                // Maybe handle RuntimeExceptions eventually.
                reversedHead.executor.execute(reversedHead.runnable);
                reversedHead = reversedHead.next;
            }

            return true;
        }
        return false;
    }

    @Override
    public void addListener(Runnable runnable, Executor executor) {
        // A lot of this code is practically copied from Guava's AbstractFuture
        checkNotNull(runnable, "Runnable was null");
        checkNotNull(executor, "Executor was null");

        Listener oldHead = listeners.get();
        if (oldHead != Listener.TOMBSTONE) {
            Listener newHead = new Listener(runnable, executor);
            do {
                newHead.next = oldHead;
                if (listeners.compareAndSet(oldHead, newHead)) {
                    return;
                }
                oldHead = listeners.get();
            } while (oldHead != Listener.TOMBSTONE);
        }

        // Maybe handle RuntimeExceptions eventually.
        executor.execute(runnable);
    }
}
