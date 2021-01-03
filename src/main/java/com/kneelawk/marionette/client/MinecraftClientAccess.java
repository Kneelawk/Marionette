package com.kneelawk.marionette.client;

import com.kneelawk.marionette.AbstractMinecraftAccess;
import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionette.api.RMIRunnable;
import com.kneelawk.marionette.api.RMIUtils;
import com.kneelawk.marionette.api.callback.ClientTickCallback;
import com.kneelawk.marionette.api.proxy.RMIConnectScreen;
import com.kneelawk.marionette.api.proxy.RMIMinecraftClient;
import com.kneelawk.marionette.api.proxy.RMIScreen;
import com.kneelawk.marionette.api.proxy.RMITitleScreen;
import com.kneelawk.marionette.client.proxy.ConnectScreenProxy;
import com.kneelawk.marionette.client.proxy.MinecraftClientProxy;
import com.kneelawk.marionette.client.proxy.ScreenProxy;
import com.kneelawk.marionette.client.proxy.TitleScreenProxy;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.TitleScreen;

import java.rmi.RemoteException;

public class MinecraftClientAccess extends AbstractMinecraftAccess implements RMIMinecraftClientAccess {
    @Override
    public void hello() {
        // Simulates doing something on the client.
        System.out.println("Hello World!");
    }

    @Override
    public void addSplashScreenCallback(RMIRunnable callback) throws RemoteException {
        ClientGlobalSignals.addSplashScreenCallback(callback);
    }

    @Override
    public void addGameJoinCallback(RMIRunnable callback) throws RemoteException {
        ClientGlobalSignals.addGameJoinCallback(callback);
    }

    @Override
    public void addClientTickCallback(ClientTickCallback callback) throws RemoteException {
        ClientGlobalQueues.pushClientTickCallback(callback);
    }

    @Override
    public RMITitleScreen newTitleScreen(CurrentThread thread) throws RemoteException {
        return RMIUtils.export(ExecutionUtils.executeIn(thread, () -> new TitleScreenProxy(new TitleScreen())));
    }

    @Override
    public RMIConnectScreen newConnectScreen(CurrentThread thread, RMIScreen parent, RMIMinecraftClient client,
                                             String address, int port) throws RemoteException {
        return RMIUtils.export(ExecutionUtils.executeIn(thread, () -> new ConnectScreenProxy(
                new ConnectScreen(((ScreenProxy) RMIUtils.requireOriginal(parent)).getProxy(),
                        ((MinecraftClientProxy) RMIUtils.requireOriginal(client)).getProxy(), address, port))));
    }
}
