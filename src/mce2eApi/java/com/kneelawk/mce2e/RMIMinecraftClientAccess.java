package com.kneelawk.mce2e;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIMinecraftClientAccess extends Remote {
    String LOOKUP_NAME = "rmi://localhost:1088/MinecraftClientAccess";

    void start() throws Throwable;

    void hello() throws RemoteException;
}
