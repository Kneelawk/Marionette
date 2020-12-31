package com.kneelawk.mce2e.instance;

import com.kneelawk.mce2e.LogWatcherThread;
import com.kneelawk.mce2e.RMIMinecraftClientAccess;

import java.rmi.RemoteException;

public class MinecraftClientInstance extends AbstractMinecraftInstance {
    private final RMIMinecraftClientAccess rmiAccess;

    public MinecraftClientInstance(Process process, LogWatcherThread outWatcher,
                                   LogWatcherThread errWatcher, RMIMinecraftClientAccess rmiAccess) {
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
