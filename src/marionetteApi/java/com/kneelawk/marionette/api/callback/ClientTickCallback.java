package com.kneelawk.marionette.api.callback;

import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.proxy.RMIMinecraftClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientTickCallback extends Remote {
    void run(CurrentThread thread, RMIMinecraftClient client) throws RemoteException;
}
