package com.kneelawk.marionette.client.proxy;

import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.RMIUtils;
import com.kneelawk.marionette.api.proxy.RMIMinecraftClient;
import com.kneelawk.marionette.api.proxy.RMIScreen;
import net.minecraft.client.MinecraftClient;

import java.rmi.RemoteException;

public class MinecraftClientProxy implements RMIMinecraftClient {
    private final MinecraftClient proxy;

    public MinecraftClientProxy(MinecraftClient proxy) {
        this.proxy = proxy;
    }

    public MinecraftClient getProxy() {
        return proxy;
    }

    @Override
    public void scheduleStop(CurrentThread thread) throws RemoteException {
        ExecutionUtils.executeIn(thread, proxy::scheduleStop);
    }

    @Override
    public void openScreen(CurrentThread thread, RMIScreen screen) throws RemoteException {
        ExecutionUtils
                .executeIn(thread, () -> proxy.openScreen(((ScreenProxy) RMIUtils.requireOriginal(screen)).getProxy()));
    }
}
