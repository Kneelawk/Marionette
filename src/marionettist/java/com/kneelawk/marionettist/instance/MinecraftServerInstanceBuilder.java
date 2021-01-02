package com.kneelawk.marionettist.instance;

import com.kneelawk.marionettist.LogWatcherThread;
import com.kneelawk.marionette.api.MarionetteConstants;
import com.kneelawk.marionettist.RMIConnectionManager;
import com.kneelawk.marionette.api.RMIMinecraftServerAccess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MinecraftServerInstanceBuilder extends AbstractMinecraftInstanceBuilder {
    private boolean eulaEnabled = true;
    private boolean guiEnabled = false;
    private final Properties serverProperties = new Properties();

    public MinecraftServerInstanceBuilder(String instanceName) {
        super(instanceName);
        serverProperties.setProperty("level-seed", "");
        serverProperties.setProperty("gamemode", "survival");
        serverProperties.setProperty("level-name", "world");
        serverProperties.setProperty("online-mode", "false");
        serverProperties.setProperty("query.port", "25565");
        serverProperties.setProperty("server-port", "25565");
    }

    public boolean isEulaEnabled() {
        return eulaEnabled;
    }

    public void setEulaEnabled(boolean eulaEnabled) {
        this.eulaEnabled = eulaEnabled;
    }

    public boolean isGuiEnabled() {
        return guiEnabled;
    }

    public void setGuiEnabled(boolean guiEnabled) {
        this.guiEnabled = guiEnabled;
    }

    public Properties getServerProperties() {
        return serverProperties;
    }

    public String getGamemode() {
        return serverProperties.getProperty("gamemode");
    }

    public void setGamemode(String gamemode) {
        serverProperties.setProperty("gamemode", gamemode);
    }

    public boolean isOnlineMode() {
        return Boolean.parseBoolean(serverProperties.getProperty("online-mode"));
    }

    public void setOnlineMode(boolean onlineMode) {
        serverProperties.setProperty("online-mode", String.valueOf(onlineMode));
    }

    public int getServerPort() {
        return Integer.parseInt(serverProperties.getProperty("server-port"));
    }

    public void setServerPort(int serverPort) {
        serverProperties.setProperty("query.port", String.valueOf(serverPort));
        serverProperties.setProperty("server-port", String.valueOf(serverPort));
    }

    public MinecraftServerInstance start(RMIConnectionManager manager) throws IOException {
        setup();

        if (eulaEnabled) {
            PrintStream eula = new PrintStream(new File(runDir, "eula.txt"));
            eula.println("eula=true");
            eula.close();
        }

        serverProperties
                .store(new FileOutputStream(new File(runDir, "server.properties")), "Minecraft server properties");

        List<String> commands = new ArrayList<>();
        commands.add("java");
        commands.add("-cp");
        commands.add(classpathString);
        commands.add("-Dfabric.dli.config=" + launchCfg);
        commands.add("-Dfabric.dli.env=server");
        commands.add("-Dfabric.dli.main=net.fabricmc.loader.launch.knot.KnotServer");
        commands.add("-D" + MarionetteConstants.ENVIRONMENT_PROPERTY + "=server");
        commands.add("-D" + MarionetteConstants.INSTANCE_NAME_PROPERTY + "=" + instanceName);
        commands.add("-D" + MarionetteConstants.RMI_PORT_PROPERTY + "=" + manager.getPort());
        commands.add("net.fabricmc.devlaunchinjector.Main");

        if (!guiEnabled) {
            commands.add("--nogui");
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

        RMIMinecraftServerAccess rmiAccess = manager.waitForBinding(instanceName);

        return new MinecraftServerInstance(minecraft, outWatcher, errWatcher, rmiAccess);
    }
}
