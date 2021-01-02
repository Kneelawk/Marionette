package com.kneelawk.marionette.api.proxy;

import com.kneelawk.marionette.api.CurrentThread;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMinecraftClient extends Remote {
    void scheduleStop(CurrentThread thread) throws RemoteException;
}
