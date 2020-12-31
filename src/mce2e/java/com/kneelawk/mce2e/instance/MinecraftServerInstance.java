package com.kneelawk.mce2e.instance;

import com.kneelawk.mce2e.LogWatcherThread;

public class MinecraftServerInstance extends AbstractMinecraftInstance {
    public MinecraftServerInstance(Process process, LogWatcherThread outWatcher,
                                   LogWatcherThread errWatcher) {
        super(process, outWatcher, errWatcher);
    }
}
