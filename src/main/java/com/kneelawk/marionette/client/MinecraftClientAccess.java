package com.kneelawk.marionette.client;

import com.kneelawk.marionette.AbstractMinecraftAccess;
import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionette.api.RMIRunnable;
import com.kneelawk.marionette.api.callback.ClientTickCallback;

import java.rmi.RemoteException;

public class MinecraftClientAccess extends AbstractMinecraftAccess implements RMIMinecraftClientAccess {
    @Override
    public void hello() {
        // Simulates doing something on the client.
        System.out.println("Hello World!");
    }

    @Override
    public void setSplashScreenCallback(RMIRunnable callback) throws RemoteException {
        ClientGlobalSignals.setSplashScreenCallback(callback);
    }

    @Override
    public void pushClientTickCallback(ClientTickCallback callback) throws RemoteException {
        ClientGlobalQueues.pushClientTickCallback(callback);
    }
}
