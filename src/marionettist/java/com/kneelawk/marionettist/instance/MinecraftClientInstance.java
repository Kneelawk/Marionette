package com.kneelawk.marionettist.instance;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionette.api.RMIUtils;
import com.kneelawk.marionette.api.callback.ClientTickCallback;
import com.kneelawk.marionette.api.proxy.RMIConnectScreen;
import com.kneelawk.marionette.api.proxy.RMIMinecraftClient;
import com.kneelawk.marionette.api.proxy.RMIScreen;
import com.kneelawk.marionette.api.proxy.RMITitleScreen;
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

    public ListenableFuture<Void> emplaceSplashScreenFuture() throws RemoteException {
        SettableFuture<Void> future = SettableFuture.create();
        rmiAccess.setSplashScreenCallback(RMIUtils.export(() -> future.set(null)));
        return future;
    }

    public ListenableFuture<Void> pushClientTickCallback(ClientTickCallback callback) throws RemoteException {
        SettableFuture<Void> future = SettableFuture.create();
        rmiAccess.pushClientTickCallback(RMIUtils.export((thread, client) -> {
            try {
                callback.run(thread, client);
                future.set(null);
            } catch (Exception e) {
                future.setException(e);
                throw e;
            }
        }));
        return future;
    }

    public RMITitleScreen newTitleScreen(CurrentThread thread) throws RemoteException {
        return rmiAccess.newTitleScreen(thread);
    }

    public RMIConnectScreen newConnectScreen(CurrentThread thread, RMIScreen parent, RMIMinecraftClient client,
                                             String address, int port) throws RemoteException {
        return rmiAccess.newConnectScreen(thread, parent, client, address, port);
    }
}
