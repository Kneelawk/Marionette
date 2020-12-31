package com.kneelawk.mce2etest;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.ExecutorService;

public abstract class AbstractMinecraftAccess {
    protected static final String NEXT_MAIN = "net.fabricmc.devlaunchinjector.Main";

    protected final ExecutorService mainThread;
    protected final String[] args;

    protected AbstractMinecraftAccess(ExecutorService mainThread, String[] args) {
        this.mainThread = mainThread;
        this.args = args;
    }

    // WHY JAVA? Why do you let me do this?
    public void start() {
        mainThread.execute(() -> {
            try {
                MethodHandle handle = MethodHandles
                        .publicLookup()
                        .findStatic(Class.forName(NEXT_MAIN), "main", MethodType.methodType(Void.TYPE, String[].class));
                handle.invokeExact(args);
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                System.out.println("Shutting down main thread...");
                mainThread.shutdown();
            }
        });
    }
}
