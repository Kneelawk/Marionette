package com.kneelawk.marionette.signal;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This signal can be called multiple times. When called, every current listener will be invoked and the list of
 * listeners will be cleared.
 */
public class ReusableSignal implements Signal {
    private final AtomicReference<Listener> listeners = new AtomicReference<>(null);

    @Override
    public boolean signal() {
        Listener head = listeners.getAndSet(null);

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

    @Override
    public void addListener(Runnable runnable, Executor executor) {
        checkNotNull(runnable, "Runnable was null");
        checkNotNull(executor, "Executor was null");

        Listener newHead = new Listener(runnable, executor);
        do {
            newHead.next = listeners.get();
        } while (!listeners.compareAndSet(newHead.next, newHead));
    }
}
