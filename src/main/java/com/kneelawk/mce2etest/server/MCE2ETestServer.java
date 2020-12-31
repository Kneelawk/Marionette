package com.kneelawk.mce2etest.server;

import com.kneelawk.mce2e.MCE2EConstants;
import com.kneelawk.mce2e.RMIMinecraftServerAccess;
import com.kneelawk.mce2etest.CurrentThreadExecutorService;
import com.kneelawk.mce2etest.ThreadWatchUnbinder;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MCE2ETestServer {
    public static void main(String[] args) throws Throwable {
        CurrentThreadExecutorService mainThread = new CurrentThreadExecutorService();
        RMIMinecraftServerAccess access = new MinecraftServerAccess(mainThread, args);
        RMIMinecraftServerAccess stub = (RMIMinecraftServerAccess) UnicastRemoteObject.exportObject(access, 0);

        ThreadWatchUnbinder unbinder = new ThreadWatchUnbinder(access, 0);

        String instanceName = System.getProperty(MCE2EConstants.INSTANCE_NAME_PROPERTY);
        int rmiPort = Integer.parseInt(System.getProperty(MCE2EConstants.RMI_PORT_PROPERTY));

        Registry registry = LocateRegistry.getRegistry(rmiPort);
        registry.bind(instanceName, stub);

        mainThread.run();

        unbinder.start();
    }
}
