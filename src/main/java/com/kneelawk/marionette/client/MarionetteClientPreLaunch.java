package com.kneelawk.marionette.client;

import com.kneelawk.marionette.AbstractMinecraftAccess;
import com.kneelawk.marionette.ThreadWatchUnbinder;
import com.kneelawk.marionette.api.RMIMinecraftClientAccess;
import com.kneelawk.marionette.api.RMIUtils;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MarionetteClientPreLaunch {
    public static AbstractMinecraftAccess setup(String instanceName, int rmiPort) {
        MinecraftClientAccess access = new MinecraftClientAccess();

        try {
            RMIMinecraftClientAccess stub = RMIUtils.<RMIMinecraftClientAccess>export(access);

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
