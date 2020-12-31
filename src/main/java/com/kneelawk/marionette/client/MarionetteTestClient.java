package com.kneelawk.marionette.client;

import com.kneelawk.marionette.api.MarionetteConstants;
import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionette.CurrentThreadExecutorService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MarionetteTestClient {
    public static void main(String[] args) throws Throwable {
        CurrentThreadExecutorService mainThread = new CurrentThreadExecutorService();
        RMIMinecraftClientAccess access = new MinecraftClientAccess(mainThread, args);
        RMIMinecraftClientAccess stub = (RMIMinecraftClientAccess) UnicastRemoteObject.exportObject(access, 0);

        String instanceName = System.getProperty(MarionetteConstants.INSTANCE_NAME_PROPERTY);
        int rmiPort = Integer.parseInt(System.getProperty(MarionetteConstants.RMI_PORT_PROPERTY));

        Registry registry = LocateRegistry.getRegistry(rmiPort);
        registry.bind(instanceName, stub);

        mainThread.run();
    }
}
