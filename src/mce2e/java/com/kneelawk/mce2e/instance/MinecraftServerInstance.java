package com.kneelawk.mce2e.instance;

import com.kneelawk.mce2e.LogWatcherThread;
import com.kneelawk.mce2e.RMIMinecraftServerAccess;

import java.rmi.RemoteException;

public class MinecraftServerInstance extends AbstractMinecraftInstance {
    private final RMIMinecraftServerAccess rmiAccess;

    public MinecraftServerInstance(Process process, LogWatcherThread outWatcher,
                                   LogWatcherThread errWatcher, RMIMinecraftServerAccess rmiAccess) {
        super(process, outWatcher, errWatcher);
        this.rmiAccess = rmiAccess;
    }

    @Override
    public void startMinecraft() throws RemoteException {
        rmiAccess.start();
    }

    public void hello() throws RemoteException {
        rmiAccess.hello();
    }
}
