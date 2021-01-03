package com.kneelawk.marionette.server;

import com.kneelawk.marionette.AbstractMinecraftAccess;
import com.kneelawk.marionette.ThreadWatchUnbinder;
import com.kneelawk.marionette.api.RMIMinecraftServerAccess;
import com.kneelawk.marionette.api.RMIUtils;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MarionetteServerPreLaunch {
    public static AbstractMinecraftAccess setup(String instanceName, int rmiPort) {
        MinecraftServerAccess access = new MinecraftServerAccess();

        try {
            RMIMinecraftServerAccess stub = RMIUtils.<RMIMinecraftServerAccess>export(access);

            ThreadWatchUnbinder unbinder = new ThreadWatchUnbinder(access);
            unbinder.start();

            Registry registry = LocateRegistry.getRegistry(rmiPort);
            registry.bind(instanceName, stub);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException("Unable to bind to marionettist!", e);
        }

        return access;
    }
}
