package com.kneelawk.mce2etest.client;

import com.kneelawk.mce2e.MCE2EConstants;
import com.kneelawk.mce2e.RMIMinecraftClientAccess;
import com.kneelawk.mce2etest.CurrentThreadExecutorService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MCE2ETestClient {
    public static void main(String[] args) throws Throwable {
        CurrentThreadExecutorService mainThread = new CurrentThreadExecutorService();
        RMIMinecraftClientAccess access = new MinecraftClientAccess(mainThread, args);
        RMIMinecraftClientAccess stub = (RMIMinecraftClientAccess) UnicastRemoteObject.exportObject(access, 0);

        String instanceName = System.getProperty(MCE2EConstants.INSTANCE_NAME_PROPERTY);
        int rmiPort = Integer.parseInt(System.getProperty(MCE2EConstants.RMI_PORT_PROPERTY));

        Registry registry = LocateRegistry.getRegistry(rmiPort);
        registry.bind(instanceName, stub);

        mainThread.run();
    }
}
