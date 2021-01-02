package com.kneelawk.marionette.api;

import java.lang.ref.WeakReference;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.WeakHashMap;

public class RMIUtils {
    private static final Map<Object, WeakReference<Object>> EXPORTS = new WeakHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends Remote> T export(T original) throws RemoteException {
        T export = (T) UnicastRemoteObject.exportObject(original, 0);
        EXPORTS.put(export, new WeakReference<>(original));
        return export;
    }

    @SuppressWarnings("unchecked")
    public static <T> T findOriginal(T export) {
        return (T) EXPORTS.get(export).get();
    }

    public static <T> T requireOriginal(T export) {
        T original = findOriginal(export);

        if (original == null) {
            throw new IllegalArgumentException(
                    "Unable to find original RMI exported object. Maybe the object was exported in another process?");
        }

        return original;
    }
}
