package com.kneelawk.marionette.server;

import com.kneelawk.marionette.AbstractMinecraftAccess;
import com.kneelawk.marionette.api.RMIMinecraftServerAccess;
import com.kneelawk.marionette.api.RMIRunnable;

import java.rmi.RemoteException;

public class MinecraftServerAccess extends AbstractMinecraftAccess implements RMIMinecraftServerAccess {
    @Override
    public void hello() {
        // Simulates doing something on the server.
        System.out.println("Hello World!");
    }

    @Override
    public void setServerStartedCallback(RMIRunnable callback) throws RemoteException {
        ServerGlobalSignals.setServerStartedCallback(callback);
    }
}
