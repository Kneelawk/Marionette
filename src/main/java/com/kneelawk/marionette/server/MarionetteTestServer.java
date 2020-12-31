package com.kneelawk.marionette.server;

import com.kneelawk.marionette.api.MarionetteConstants;
import com.kneelawk.marionette.api.RMIMinecraftServerAccess;
import com.kneelawk.marionette.CurrentThreadExecutorService;
import com.kneelawk.marionette.ThreadWatchUnbinder;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MarionetteTestServer {
    public static void main(String[] args) throws Throwable {
        CurrentThreadExecutorService mainThread = new CurrentThreadExecutorService();
        RMIMinecraftServerAccess access = new MinecraftServerAccess(mainThread, args);
        RMIMinecraftServerAccess stub = (RMIMinecraftServerAccess) UnicastRemoteObject.exportObject(access, 0);

        ThreadWatchUnbinder unbinder = new ThreadWatchUnbinder(access, 0);

        String instanceName = System.getProperty(MarionetteConstants.INSTANCE_NAME_PROPERTY);
        int rmiPort = Integer.parseInt(System.getProperty(MarionetteConstants.RMI_PORT_PROPERTY));

        Registry registry = LocateRegistry.getRegistry(rmiPort);
        registry.bind(instanceName, stub);

        mainThread.run();

        unbinder.start();
    }
}
