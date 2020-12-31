package com.kneelawk.mce2e.instance;

import com.kneelawk.mce2e.LogWatcherThread;

public class MinecraftClientInstance extends AbstractMinecraftInstance {
    public MinecraftClientInstance(Process process, LogWatcherThread outWatcher,
                                   LogWatcherThread errWatcher) {
        super(process, outWatcher, errWatcher);
    }
}
