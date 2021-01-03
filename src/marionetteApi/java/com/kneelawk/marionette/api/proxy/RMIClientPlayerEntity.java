package com.kneelawk.marionette.api.proxy;

import com.kneelawk.marionette.api.CurrentThread;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientPlayerEntity extends Remote {
    void sendChatMessage(CurrentThread thread, String message) throws RemoteException;
}
