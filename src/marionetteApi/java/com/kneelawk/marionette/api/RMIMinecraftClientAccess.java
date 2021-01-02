package com.kneelawk.marionette.api;

import com.kneelawk.marionette.api.callback.ClientTickCallback;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMinecraftClientAccess extends Remote {
    void start() throws RemoteException;

    void hello() throws RemoteException;

    void setSplashScreenCallback(RMIRunnable callback) throws RemoteException;

    void pushClientTickCallback(ClientTickCallback callback) throws RemoteException;
}
