package com.kneelawk.mce2etest.client;

import com.kneelawk.mce2e.RMIMinecraftClientAccess;
import com.kneelawk.mce2etest.AbstractMinecraftAccess;

import java.util.concurrent.ExecutorService;

public class MinecraftClientAccess extends AbstractMinecraftAccess implements RMIMinecraftClientAccess {
    public MinecraftClientAccess(ExecutorService mainThread, String[] args) {
        super(mainThread, args);
    }

    @Override
    public void hello() {
        // Simulates doing something on the client.
        System.out.println("Hello World!");
    }
}
