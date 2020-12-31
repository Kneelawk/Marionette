package com.kneelawk.marionette.client;

import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionette.AbstractMinecraftAccess;

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
