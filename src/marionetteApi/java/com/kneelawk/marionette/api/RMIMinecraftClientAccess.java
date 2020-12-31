package com.kneelawk.marionette.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMinecraftClientAccess extends Remote {
    void start() throws RemoteException;

    void hello() throws RemoteException;
}
