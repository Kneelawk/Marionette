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

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Client #");
        System.out.println("#############################");
        MinecraftClientInstance minecraft = minecraftBuilder.start();

        minecraft.finish();

        System.out.println("#############################");
        System.out.println("# Minecraft Client finished #");
        System.out.println("#############################");
    }

    @Test
    void startServer() throws IOException, InterruptedException {
        MinecraftServerInstanceBuilder minecraftBuilder = new MinecraftServerInstanceBuilder("server");

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Server #");
        System.out.println("#############################");
        MinecraftServerInstance minecraft = minecraftBuilder.start();

        // make sure the server stops
        PrintStream serverInput = new PrintStream(minecraft.getProcess().getOutputStream());
        serverInput.println("/stop");
        serverInput.flush();

        minecraft.finish();

        System.out.println("#############################");
        System.out.println("# Minecraft Server finished #");
        System.out.println("#############################");
    }

    @Test
    void startBoth() throws IOException, InterruptedException {
        MinecraftServerInstanceBuilder serverBuilder = new MinecraftServerInstanceBuilder("server1");
        MinecraftClientInstanceBuilder clientBuilder = new MinecraftClientInstanceBuilder("client1");

        System.out.println("#######################################");
        System.out.println("# Starting Minecrft Server and Client #");
        System.out.println("#######################################");

        MinecraftServerInstance server = serverBuilder.start();
        MinecraftClientInstance client = clientBuilder.start();

        PrintStream serverInput = new PrintStream(server.getProcess().getOutputStream());
        serverInput.println("/op client1");
        serverInput.flush();

        // Here, the player is expected to connect to the server at local host and execute the command `/stop` to stop the server, then close the client.

        server.finish();
        client.finish();

        System.out.println("#######################################");
        System.out.println("# Minecrft Server and Client finished #");
        System.out.println("#######################################");
    }
}
