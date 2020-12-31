package com.kneelawk.marionette.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CurrentThread extends Remote {
    boolean isExpired() throws RemoteException;
}
