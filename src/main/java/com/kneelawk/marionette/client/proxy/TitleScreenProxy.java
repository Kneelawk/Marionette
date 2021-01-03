package com.kneelawk.marionette.client.proxy;

import com.kneelawk.marionette.api.proxy.RMITitleScreen;
import net.minecraft.client.gui.screen.TitleScreen;

public class TitleScreenProxy implements RMITitleScreen, ScreenProxy {
    private final TitleScreen proxy;

    public TitleScreenProxy(TitleScreen proxy) {
        this.proxy = proxy;
    }

    public TitleScreen getProxy() {
        return proxy;
    }
}
