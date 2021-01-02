package com.kneelawk.marionette;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

public class ThreadWatchUnbinder extends Thread {
    private final Remote toUnbind;

    public ThreadWatchUnbinder(Remote toUnbind) {
        this.toUnbind = toUnbind;

        setDaemon(true);
        setName("RMI-Unbinder");
    }

    @Override
    public void run() {
        while (true) {
            Set<Thread> remainingThreads = new HashSet<>(Thread.getAllStackTraces().keySet());

            // remove all daemon threads
            remainingThreads.removeIf(Thread::isDaemon);

            // remove the jvm shutdown and rmi keepalive thread
            remainingThreads.removeIf(thread -> "DestroyJavaVM".equals(thread.getName()));
            remainingThreads
                    .removeIf(thread -> thread.getName() != null && thread.getName().toLowerCase().contains("rmi"));

            if (remainingThreads.isEmpty()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            System.out.println("Unbinding RMI object...");
            UnicastRemoteObject.unexportObject(toUnbind, true);
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }
}
