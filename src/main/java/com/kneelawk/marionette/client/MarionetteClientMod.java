package com.kneelawk.marionette.client;

import com.kneelawk.marionette.ExecutionUtils;
import com.kneelawk.marionette.api.RMIUtils;
import com.kneelawk.marionette.client.proxy.MinecraftClientProxy;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class MarionetteClientMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> ExecutionUtils
                .pollQueue(ClientGlobalQueues::pollClientTickCallbacks, (callback, currentThread) -> callback
                        .run(RMIUtils.export(currentThread), RMIUtils.export(new MinecraftClientProxy(client)))));
    }
}
