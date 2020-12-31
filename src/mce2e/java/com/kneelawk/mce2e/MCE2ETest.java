package com.kneelawk.mce2e;

import com.kneelawk.mce2e.instance.MinecraftClientInstance;
import com.kneelawk.mce2e.instance.MinecraftClientInstanceBuilder;
import com.kneelawk.mce2e.instance.MinecraftServerInstance;
import com.kneelawk.mce2e.instance.MinecraftServerInstanceBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintStream;

public class MCE2ETest {
    @Test
    void startClient() throws IOException, InterruptedException {
        MinecraftClientInstanceBuilder minecraftBuilder = new MinecraftClientInstanceBuilder("client");
        RMIConnectionManager manager = new RMIConnectionManager();

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Client #");
        System.out.println("#############################");
        MinecraftClientInstance minecraft = minecraftBuilder.start(manager);

        System.out.println("Calling startMinecraft()");
        minecraft.startMinecraft();

        System.out.println("Sleeping for 7 seconds");
        Thread.sleep(7000);

        System.out.println("Calling hello()");
        minecraft.hello();

        System.out.println("Calling finish()");
        minecraft.finish();

        System.out.println("Shutting down the RMI server...");
        manager.shutdown();

        System.out.println("#############################");
        System.out.println("# Minecraft Client finished #");
        System.out.println("#############################");
    }

    @Test
    void startServer() throws IOException, InterruptedException {
        MinecraftServerInstanceBuilder minecraftBuilder = new MinecraftServerInstanceBuilder("server");
        RMIConnectionManager manager = new RMIConnectionManager();

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Server #");
        System.out.println("#############################");
        MinecraftServerInstance minecraft = minecraftBuilder.start(manager);

        System.out.println("Calling startMinecraft()");
        minecraft.startMinecraft();

        System.out.println("Sleeping for 7 seconds");
        Thread.sleep(7000);

        System.out.println("Calling hello()");
        minecraft.hello();

        // make sure the server stops
        System.out.println("Sending the server /stop command...");
        PrintStream serverInput = new PrintStream(minecraft.getProcess().getOutputStream());
        serverInput.println("/stop");
        serverInput.flush();

        System.out.println("Calling finish()");
        minecraft.finish();

        System.out.println("Shutting down the RMI server...");
        manager.shutdown();

        System.out.println("#############################");
        System.out.println("# Minecraft Server finished #");
        System.out.println("#############################");
    }

    @Test
    void startBoth() throws IOException, InterruptedException {
        MinecraftServerInstanceBuilder serverBuilder = new MinecraftServerInstanceBuilder("server1");
        MinecraftClientInstanceBuilder clientBuilder = new MinecraftClientInstanceBuilder("client1");

        RMIConnectionManager manager = new RMIConnectionManager();

        System.out.println("#######################################");
        System.out.println("# Starting Minecrft Server and Client #");
        System.out.println("#######################################");
        System.out.println("# Here, the player is expected to     #");
        System.out.println("# connect to the server at local host #");
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

        server.finish();
        client.finish();

        manager.shutdown();

        System.out.println("#######################################");
        System.out.println("# Minecrft Server and Client finished #");
        System.out.println("#######################################");
    }
}
