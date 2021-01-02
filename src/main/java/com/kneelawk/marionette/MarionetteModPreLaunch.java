package com.kneelawk.marionette;

import com.kneelawk.marionette.api.MarionetteConstants;
import com.kneelawk.marionette.client.MarionetteClientPreLaunch;
import com.kneelawk.marionette.server.MarionetteServerPreLaunch;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class MarionetteModPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        String env = requiredProperty(MarionetteConstants.ENVIRONMENT_PROPERTY);
        String instanceName = requiredProperty(MarionetteConstants.INSTANCE_NAME_PROPERTY);
        int rmiPort = Integer.parseInt(requiredProperty(MarionetteConstants.RMI_PORT_PROPERTY));

        AbstractMinecraftAccess access;
        if ("client".equals(env)) {
            access = MarionetteClientPreLaunch.setup(instanceName, rmiPort);
        } else if ("server".equals(env)) {
            access = MarionetteServerPreLaunch.setup(instanceName, rmiPort);
        } else {
            throw new IllegalArgumentException("Unknown fabric.dli.env value: " + env);
        }

        try {
            access.awaitStart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String requiredProperty(String name) {
        String value = System.getProperty(name);
        if (value == null) {
            throw new IllegalArgumentException("Property `" + name + "` is missing. Marionette cannot start.");
        }
        return value;
    }
}
