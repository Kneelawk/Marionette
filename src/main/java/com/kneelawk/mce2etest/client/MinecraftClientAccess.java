package com.kneelawk.mce2etest.client;

import com.kneelawk.mce2e.RMIMinecraftClientAccess;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MinecraftClientAccess implements RMIMinecraftClientAccess {
    private static final String NEXT_MAIN = "net.fabricmc.devlaunchinjector.Main";

    private final String[] args;

    public MinecraftClientAccess(String[] args) {
        this.args = args;
    }

    @Override
    public void start() throws Throwable {
        MethodHandle handle = MethodHandles
                .publicLookup()
                .findStatic(Class.forName(NEXT_MAIN), "main", MethodType.methodType(Void.TYPE, String[].class));
        handle.invokeExact(args);
    }

    @Override
    public void hello() {
        System.out.println("Hello World");
    }
}
