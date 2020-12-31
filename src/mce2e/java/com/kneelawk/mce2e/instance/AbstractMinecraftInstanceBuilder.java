package com.kneelawk.mce2e.instance;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class AbstractMinecraftInstanceBuilder {
    protected String instanceName;
    protected boolean cleanedRunDir = true;
    protected boolean handledIO = true;

    /*
     * Fields initialized by setup().
     */
    protected String classpathString;
    protected File launchCfg;
    protected File runDir;

    public AbstractMinecraftInstanceBuilder(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public boolean isCleanedRunDir() {
        return cleanedRunDir;
    }

    public void setCleanedRunDir(boolean cleanedRunDir) {
        this.cleanedRunDir = cleanedRunDir;
    }

    public boolean isHandledIO() {
        return handledIO;
    }

    public void setHandledIO(boolean handledIO) {
        this.handledIO = handledIO;
    }

    protected void setup() throws IOException {
        classpathString = System.getProperty("com.kneelawk.mce2e.minecraft_classpath");
        File projectDir = new File(System.getProperty("com.kneelawk.mce2e.project_root_dir"));
        File buildDir = new File(System.getProperty("com.kneelawk.mce2e.project_build_dir"));
        File mce2eDir = new File(buildDir, "mce2e");
        File instancesDir = new File(mce2eDir, "instances");
        File instanceDir = new File(instancesDir, instanceName);
        File configsDir = new File(instanceDir, "configs");
        File log4jCfg = new File(configsDir, "log4j.xml");
        launchCfg = new File(configsDir, "launch.cfg");
        runDir = new File(instanceDir, "run");

        if (!configsDir.exists()) {
            assertTrue(configsDir.mkdirs(), "Failed to create configs directory");
        }
        writeLog4jConfig(log4jCfg);
        writeLaunchConfig(launchCfg, log4jCfg, projectDir);

        if (runDir.exists() && cleanedRunDir) {
            FileUtils.deleteDirectory(runDir);
        }
        if (!runDir.mkdirs()) {
            throw new IOException("Failed to create the run directory");
        }
    }

    public abstract AbstractMinecraftInstance start() throws IOException;

    private static void writeLog4jConfig(File log4jCfg) throws IOException {
        FileUtils.copyToFile(AbstractMinecraftInstanceBuilder.class.getResourceAsStream("log4j.xml"), log4jCfg);
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
