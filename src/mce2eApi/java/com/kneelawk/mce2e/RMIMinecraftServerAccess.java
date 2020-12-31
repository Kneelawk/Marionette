package com.kneelawk.mce2e;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMinecraftServerAccess extends Remote {
    void start() throws RemoteException;

    void hello() throws RemoteException;
}
