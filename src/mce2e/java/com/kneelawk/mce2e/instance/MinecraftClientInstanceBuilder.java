package com.kneelawk.mce2e.instance;

import com.kneelawk.mce2e.MCE2EConstants;
import com.kneelawk.mce2e.RMIConnectionManager;
import com.kneelawk.mce2e.LogWatcherThread;
import com.kneelawk.mce2e.RMIMinecraftClientAccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinecraftClientInstanceBuilder extends AbstractMinecraftInstanceBuilder {
    private String username;

    public MinecraftClientInstanceBuilder(String instanceName) {
        super(instanceName);
        username = instanceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public MinecraftClientInstance start(RMIConnectionManager manager) throws IOException {
        setup();

        List<String> commands = new ArrayList<>();
        commands.add("java");
        commands.add("-cp");
        commands.add(classpathString);
        commands.add("-Dfabric.dli.config=" + launchCfg);
        commands.add("-Dfabric.dli.env=client");
        commands.add("-Dfabric.dli.main=net.fabricmc.loader.launch.knot.KnotClient");
        commands.add("-D" + MCE2EConstants.INSTANCE_NAME_PROPERTY + "=" + instanceName);
        commands.add("-D" + MCE2EConstants.RMI_PORT_PROPERTY + "=" + manager.getPort());
        commands.add("com.kneelawk.mce2etest.client.MCE2ETestClient");

        if (username != null) {
            commands.add("--username");
            commands.add(username);
        }

        ProcessBuilder minecraftBuilder = new ProcessBuilder(commands);
        minecraftBuilder.directory(runDir);

        Process minecraft = minecraftBuilder.start();
        parentProcess(minecraft);

        LogWatcherThread outWatcher =
                new LogWatcherThread(minecraft.getInputStream(), System.out, "[" + instanceName + "] ");
        outWatcher.start();
        LogWatcherThread errWatcher =
                new LogWatcherThread(minecraft.getErrorStream(), System.err, "[" + instanceName + "] ");
        errWatcher.start();

        RMIMinecraftClientAccess rmiAccess = manager.waitForBinding(instanceName);

        return new MinecraftClientInstance(minecraft, outWatcher, errWatcher, rmiAccess);
    }
}
