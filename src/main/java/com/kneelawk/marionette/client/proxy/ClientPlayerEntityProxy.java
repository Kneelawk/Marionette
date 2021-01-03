package com.kneelawk.marionette.client.proxy;

import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.api.CurrentThread;
import com.kneelawk.marionette.api.proxy.RMIClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;

import java.rmi.RemoteException;

public class ClientPlayerEntityProxy implements RMIClientPlayerEntity {
    private final ClientPlayerEntity proxy;

    public ClientPlayerEntityProxy(ClientPlayerEntity proxy) {
        this.proxy = proxy;
    }

    @Override
    public void sendChatMessage(CurrentThread thread, String message) throws RemoteException {
        ExecutionUtils.executeIn(thread, () -> proxy.sendChatMessage(message));
    }
}
