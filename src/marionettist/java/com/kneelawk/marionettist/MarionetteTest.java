package com.kneelawk.marionettist;

import com.kneelawk.marionettist.instance.MinecraftClientInstance;
import com.kneelawk.marionettist.instance.MinecraftClientInstanceBuilder;
import com.kneelawk.marionettist.instance.MinecraftServerInstance;
import com.kneelawk.marionettist.instance.MinecraftServerInstanceBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

public class MarionetteTest {
    private static RMIConnectionManager manager;

    @BeforeAll
    static void startRMI() throws RemoteException {
        manager = new RMIConnectionManager();
    }

    @AfterAll
    static void stopRMI() {
        manager.shutdown();
    }

    @Test
    void startClient() throws IOException, InterruptedException, ExecutionException {
        MinecraftClientInstanceBuilder minecraftBuilder = new MinecraftClientInstanceBuilder("client");

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Client #");
        System.out.println("#############################");
        MinecraftClientInstance minecraft = minecraftBuilder.start(manager);

        System.out.println("Calling startMinecraft()");
        minecraft.startMinecraft();

        System.out.println("Waiting for minecraft to start...");
        minecraft.emplaceSplashScreenFuture().get();

        System.out.println("Calling hello()");
        minecraft.hello();

        System.out.println("Shutting down the client...");
        minecraft.pushClientTickCallback((currentThread, client) -> client.scheduleStop(currentThread)).get();

        System.out.println("Calling finish()");
        minecraft.finish();

        System.out.println("#############################");
        System.out.println("# Minecraft Client finished #");
        System.out.println("#############################");
    }

    @Test
    void startServer() throws IOException, InterruptedException, ExecutionException {
        MinecraftServerInstanceBuilder minecraftBuilder = new MinecraftServerInstanceBuilder("server");

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Server #");
        System.out.println("#############################");
        MinecraftServerInstance minecraft = minecraftBuilder.start(manager);

        System.out.println("Calling startMinecraft()");
        minecraft.startMinecraft();

        System.out.println("Waiting for minecraft to start...");
        minecraft.emplaceServerStartedFuture().get();

        System.out.println("Calling hello()");
        minecraft.hello();

        // make sure the server stops
        System.out.println("Sending the server /stop command...");
        PrintStream serverInput = new PrintStream(minecraft.getProcess().getOutputStream());
        serverInput.println("/stop");
        serverInput.flush();

        System.out.println("Calling finish()");
        minecraft.finish();

        System.out.println("#############################");
        System.out.println("# Minecraft Server finished #");
        System.out.println("#############################");
    }

    @Test
    void startBoth() throws IOException, InterruptedException, ExecutionException {
        MinecraftServerInstanceBuilder serverBuilder = new MinecraftServerInstanceBuilder("server1");
        MinecraftClientInstanceBuilder clientBuilder = new MinecraftClientInstanceBuilder("client1");

        serverBuilder.setGamemode("creative");

        System.out.println("#######################################");
        System.out.println("# Starting Minecrft Server and Client #");
        System.out.println("#######################################");
        System.out.println("# Here, the player is expected to     #");
        System.out.println("# connect to the server at localhost  #");
        System.out.println("# and execute the command `/stop` to  #");
        System.out.println("# stop the server, then close the     #");
        System.out.println("# client.                             #");
        System.out.println("#######################################");

        MinecraftServerInstance server = serverBuilder.start(manager);
        MinecraftClientInstance client = clientBuilder.start(manager);

        server.startMinecraft();
        client.startMinecraft();

        PrintStream serverInput = new PrintStream(server.getProcess().getOutputStream());
        serverInput.println("/op client1");
        serverInput.flush();

        System.out.println("Waiting for client to start up...");
        client.emplaceSplashScreenFuture().get();

        System.out.println("Waiting for server to start up...");
        server.emplaceServerStartedFuture().get();

        System.out.println("Connecting to server...");
        client.pushClientTickCallback((thread, client1) -> {
            client1.openScreen(thread,
                    client.newConnectScreen(thread, client.newTitleScreen(thread), client1, "localhost", 25565));
        }).get();

        server.finish();
        client.finish();

        System.out.println("#######################################");
        System.out.println("# Minecrft Server and Client finished #");
        System.out.println("#######################################");
    }
}
