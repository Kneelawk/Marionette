package com.kneelawk.marionette.server;

import com.kneelawk.marionette.api.RMIMinecraftServerAccess;
import com.kneelawk.marionette.AbstractMinecraftAccess;

import java.util.concurrent.ExecutorService;

public class MinecraftServerAccess extends AbstractMinecraftAccess implements RMIMinecraftServerAccess {
    @Override
    public void hello() {
        // Simulates doing something on the server.
        System.out.println("Hello World!");
    }
}
