package com.kneelawk.marionettist.instance;

import com.kneelawk.marionettist.LogWatcherThread;
import com.kneelawk.marionette.api.RMIMinecraftServerAccess;

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
