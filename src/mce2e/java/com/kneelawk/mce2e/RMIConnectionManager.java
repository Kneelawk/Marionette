package com.kneelawk.mce2e;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIConnectionManager {
    private final int port;
    private final Registry registry;

    public RMIConnectionManager() throws RemoteException {
        this(25599);
    }

    public RMIConnectionManager(int port) throws RemoteException {
        this.port = port;
        registry = LocateRegistry.createRegistry(port);
    }

    public int getPort() {
        return port;
    }

    public Registry getRegistry() {
        return registry;
    }

    @SuppressWarnings("unchecked")
    public <T> T waitForBinding(String name) throws RemoteException {
        T binding = null;

        while (binding == null) {
            try {
                binding = (T) registry.lookup(name);
            } catch (NotBoundException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }

        return binding;
    }

    public void shutdown() {
        try {
            UnicastRemoteObject.unexportObject(registry, true);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException("This shouldn't happen", e);
        }
    }
}
