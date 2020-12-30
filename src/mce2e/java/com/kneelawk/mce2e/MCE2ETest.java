package com.kneelawk.mce2e;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MCE2ETest {
    @Test
    void startClient() throws IOException, InterruptedException {
        String classpathString = System.getProperty("com.kneelawk.mce2e.minecraft_classpath");
        File projectDir = new File(System.getProperty("com.kneelawk.mce2e.project_root_dir"));
        File buildDir = new File(System.getProperty("com.kneelawk.mce2e.project_build_dir"));
        File mce2eDir = new File(buildDir, "mce2e");
        File configsDir = new File(mce2eDir, "configs");
        File log4jCfg = new File(configsDir, "log4j.xml");
        File launchCfg = new File(configsDir, "launch.cfg");
        File runDir = new File(mce2eDir, "runs/client-run");

        if (!configsDir.exists()) {
            assertTrue(configsDir.mkdirs(), "Failed to create configs directory");
        }
        writeLog4jConfig(log4jCfg);
        writeLaunchConfig(launchCfg, log4jCfg, projectDir);

        if (runDir.exists()) {
            FileUtils.deleteDirectory(runDir);
        }
        assertTrue(runDir.mkdirs(), "Failed to create the run directory");

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Client #");
        System.out.println("#############################");
        ProcessBuilder minecraftBuilder =
                new ProcessBuilder("java", "-cp", classpathString, "-Dfabric.dli.config=" + launchCfg,
                        "-Dfabric.dli.env=client", "-Dfabric.dli.main=net.fabricmc.loader.launch.knot.KnotClient",
                        "com.kneelawk.mce2etest.client.MCE2ETestClient");
        minecraftBuilder.directory(runDir);

        Process minecraft = minecraftBuilder.start();
        LogWatcherThread watcher = new LogWatcherThread(minecraft.getInputStream(), System.out);
        watcher.start();
        new IOCopyThread(minecraft.getErrorStream(), System.err).start();

        assertEquals(0, minecraft.waitFor(), "Minecraft failed to shutdown correctly! See console for details.");
        watcher.checkError();

        System.out.println("#############################");
        System.out.println("# Minecraft Client finished #");
        System.out.println("#############################");
    }

    @Test
    void startServer() throws IOException, InterruptedException {
        String classpathString = System.getProperty("com.kneelawk.mce2e.minecraft_classpath");
        File projectDir = new File(System.getProperty("com.kneelawk.mce2e.project_root_dir"));
        File buildDir = new File(System.getProperty("com.kneelawk.mce2e.project_build_dir"));
        File mce2eDir = new File(buildDir, "mce2e");
        File configsDir = new File(mce2eDir, "configs");
        File log4jCfg = new File(configsDir, "log4j.xml");
        File launchCfg = new File(configsDir, "launch.cfg");
        File runDir = new File(mce2eDir, "runs/server-run");

        if (!configsDir.exists()) {
            assertTrue(configsDir.mkdirs(), "Failed to create configs directory");
        }
        writeLog4jConfig(log4jCfg);
        writeLaunchConfig(launchCfg, log4jCfg, projectDir);

        if (runDir.exists()) {
            FileUtils.deleteDirectory(runDir);
        }
        assertTrue(runDir.mkdirs(), "Failed to create the run directory");

        PrintStream eula = new PrintStream(new File(runDir, "eula.txt"));
        eula.println("eula=true");
        eula.close();

        System.out.println("#############################");
        System.out.println("# Starting Minecraft Server #");
        System.out.println("#############################");
        ProcessBuilder minecraftBuilder =
                new ProcessBuilder("java", "-cp", classpathString, "-Dfabric.dli.config=" + launchCfg,
                        "-Dfabric.dli.env=server", "-Dfabric.dli.main=net.fabricmc.loader.launch.knot.KnotServer",
                        "com.kneelawk.mce2etest.server.MCE2ETestServer", "--nogui");
        minecraftBuilder.directory(runDir);

        Process minecraft = minecraftBuilder.start();
        LogWatcherThread watcher = new LogWatcherThread(minecraft.getInputStream(), System.out);
        watcher.start();
        new IOCopyThread(minecraft.getErrorStream(), System.err).start();

        // make sure the server stops
        PrintStream serverInput = new PrintStream(minecraft.getOutputStream());
        serverInput.println("/stop");
        serverInput.flush();

        assertEquals(0, minecraft.waitFor(), "Minecraft failed to shutdown correctly! See console for details.");
        watcher.checkError();

        System.out.println("#############################");
        System.out.println("# Minecraft Server finished #");
        System.out.println("#############################");
    }

    private static void writeLog4jConfig(File log4jCfg) throws IOException {
        FileUtils.copyToFile(MCE2ETest.class.getResourceAsStream("log4j.xml"), log4jCfg);
    }

    private static void writeLaunchConfig(File launchCfg, File log4jCfg, File projectDir) throws IOException {
        File originalLaunchCfg = new File(projectDir, ".gradle/loom-cache/launch.cfg");
        String log4jCfgKey = "log4j.configurationFile=";
        Scanner originalCfg = new Scanner(originalLaunchCfg);
        PrintStream cfg = new PrintStream(launchCfg);

        while (originalCfg.hasNextLine()) {
            String line = originalCfg.nextLine();
            int index = line.indexOf(log4jCfgKey);
            if (index != -1) {
                cfg.println(line.substring(0, index + log4jCfgKey.length()) + log4jCfg);
            } else {
                cfg.println(line);
            }
        }

        cfg.close();
    }

}
