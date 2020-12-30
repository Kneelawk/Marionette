package com.kneelawk.mce2e;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CurrentThread extends Remote {
    boolean isExpired() throws RemoteException;
}
