package com.kneelawk.marionettist.instance;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.kneelawk.marionette.api.RMIMinecraftServerAccess;
import com.kneelawk.marionette.api.RMIUtils;
import com.kneelawk.marionettist.LogWatcherThread;

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

    public ListenableFuture<Void> emplaceServerStartedFuture() throws RemoteException {
        SettableFuture<Void> future = SettableFuture.create();
        rmiAccess.setServerStartedCallback(RMIUtils.export(() -> future.set(null)));
        return future;
    }
}
