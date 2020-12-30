package com.kneelawk.mce2etest.server;

import com.kneelawk.mce2e.RMIMinecraftServerAccess;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class MinecraftServerAccess implements RMIMinecraftServerAccess {
    private static final String NEXT_MAIN = "net.fabricmc.devlaunchinjector.Main";

    private final String[] args;

    public MinecraftServerAccess(String[] args) {
        this.args = args;
    }

    @Override
    public void start() throws Throwable {
        MethodHandle handle = MethodHandles
                .publicLookup()
                .findStatic(Class.forName(NEXT_MAIN), "main", MethodType.methodType(Void.TYPE, String[].class));
        handle.invokeExact(args);
    }
}
