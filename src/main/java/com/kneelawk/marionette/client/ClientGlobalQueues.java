package com.kneelawk.marionette.client;

import com.kneelawk.marionette.api.callback.ClientTickCallback;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientGlobalQueues {
    private static final BlockingQueue<ClientTickCallback> CLIENT_TICK_CALLBACKS = new LinkedBlockingQueue<>();

    public static ClientTickCallback pollClientTickCallbacks() {
        return CLIENT_TICK_CALLBACKS.poll();
    }

    public static void pushClientTickCallback(ClientTickCallback callback) {
        try {
            CLIENT_TICK_CALLBACKS.put(callback);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
