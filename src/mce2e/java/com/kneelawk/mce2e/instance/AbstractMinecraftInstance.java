package com.kneelawk.mce2e.instance;

import com.kneelawk.mce2e.LogWatcherThread;

public abstract class AbstractMinecraftInstance {
    protected final Process process;
    protected final LogWatcherThread outWatcher;
    protected final LogWatcherThread errWatcher;

    public AbstractMinecraftInstance(Process process, LogWatcherThread outWatcher,
                                     LogWatcherThread errWatcher) {
        this.process = process;
        this.outWatcher = outWatcher;
        this.errWatcher = errWatcher;
    }

    public Process getProcess() {
        return process;
    }

    public void finish() throws InterruptedException {
        if (process.waitFor() != 0) {
            // TODO: Find a better exception for this.
            throw new RuntimeException("Minecraft exited with non-zero exit code.");
        }

        outWatcher.checkError();
        errWatcher.checkError();
    }
}