package com.kneelawk.mce2etest;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

public class ThreadWatchUnbinder extends Thread {
    private final Remote toUnbind;

    private final Set<Thread> initialThreads;
    private final int finalThreadCount;

    public ThreadWatchUnbinder(Remote toUnbind, int finalThreadCount) {
        this.toUnbind = toUnbind;
        this.finalThreadCount = finalThreadCount;

        setDaemon(true);
        setName("RMI-Unbinder");

        initialThreads = new HashSet<>(Thread.getAllStackTraces().keySet());
    }

    @Override
    public void run() {
        while (true) {
            Set<Thread> remainingThreads = new HashSet<>(Thread.getAllStackTraces().keySet());

            // remove all daemon threads
            remainingThreads.removeIf(thread -> thread.isDaemon());

            // remove the jvm shutdown keepalive thread
            remainingThreads.removeIf(thread -> "DestroyJavaVM".equals(thread.getName()));

            // remove all the threads we knew about when we were created (hopefully including the rmi threads)
            remainingThreads.removeAll(initialThreads);

            if (remainingThreads.size() <= finalThreadCount) {
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
