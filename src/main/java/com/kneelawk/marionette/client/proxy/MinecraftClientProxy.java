package com.kneelawk.marionette.client.proxy;

import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.proxy.RMIMinecraftClient;
import net.minecraft.client.MinecraftClient;

import java.rmi.RemoteException;

public class MinecraftClientProxy implements RMIMinecraftClient {
    private MinecraftClient proxy;

    public MinecraftClientProxy(MinecraftClient proxy) {
        this.proxy = proxy;
    }

    @Override
    public void scheduleStop(CurrentThread thread) throws RemoteException {
        ExecutionUtils.executeIn(thread, () -> proxy.scheduleStop());
    }
}
