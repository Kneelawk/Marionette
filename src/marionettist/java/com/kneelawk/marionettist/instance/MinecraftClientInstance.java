package com.kneelawk.marionettist.instance;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionette.api.RMIUtils;
import com.kneelawk.marionette.api.callback.ClientTickCallback;
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

    public ListenableFuture<Void> getSplashScreenFuture() throws RemoteException {
        SettableFuture<Void> future = SettableFuture.create();
        rmiAccess.setSplashScreenCallback(RMIUtils.export(() -> future.set(null)));
        return future;
    }

    public void pushClientTickCallback(ClientTickCallback callback) throws RemoteException {
        rmiAccess.pushClientTickCallback(RMIUtils.export(callback));
    }
}
