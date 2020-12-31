package com.kneelawk.marionettist.instance;

import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionettist.LogWatcherThread;

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
