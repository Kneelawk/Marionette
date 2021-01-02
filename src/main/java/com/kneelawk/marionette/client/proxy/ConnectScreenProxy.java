package com.kneelawk.marionette.client.proxy;

import com.kneelawk.marionette.api.proxy.RMIConnectScreen;
import net.minecraft.client.gui.screen.ConnectScreen;

public class ConnectScreenProxy implements RMIConnectScreen, ScreenProxy {
    private final ConnectScreen proxy;

    public ConnectScreenProxy(ConnectScreen proxy) {
        this.proxy = proxy;
    }

    public ConnectScreen getProxy() {
        return proxy;
    }
}
