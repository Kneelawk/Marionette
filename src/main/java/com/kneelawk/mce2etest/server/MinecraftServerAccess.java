package com.kneelawk.mce2etest.server;

import com.kneelawk.mce2e.RMIMinecraftServerAccess;
import com.kneelawk.mce2etest.AbstractMinecraftAccess;

import java.util.concurrent.ExecutorService;

public class MinecraftServerAccess extends AbstractMinecraftAccess implements RMIMinecraftServerAccess {
    protected MinecraftServerAccess(ExecutorService mainThread, String[] args) {
        super(mainThread, args);
    }

    @Override
    public void hello() {
        // Simulates doing something on the server.
        System.out.println("Hello World!");
    }
}
