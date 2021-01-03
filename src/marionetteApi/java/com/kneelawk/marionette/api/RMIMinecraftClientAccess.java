package com.kneelawk.marionette.api;

import com.kneelawk.marionette.api.callback.ClientTickCallback;
import com.kneelawk.marionette.api.proxy.RMIConnectScreen;
import com.kneelawk.marionette.api.proxy.RMIMinecraftClient;
import com.kneelawk.marionette.api.proxy.RMIScreen;
import com.kneelawk.marionette.api.proxy.RMITitleScreen;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMinecraftClientAccess extends Remote {
    void start() throws RemoteException;

    void hello() throws RemoteException;

    void addSplashScreenCallback(RMIRunnable callback) throws RemoteException;

    void addGameJoinCallback(RMIRunnable callback) throws RemoteException;

    void addClientTickCallback(ClientTickCallback callback) throws RemoteException;

    RMITitleScreen newTitleScreen(CurrentThread thread) throws RemoteException;

    RMIConnectScreen newConnectScreen(CurrentThread thread, RMIScreen parent, RMIMinecraftClient client, String address,
                                      int port) throws RemoteException;
}
