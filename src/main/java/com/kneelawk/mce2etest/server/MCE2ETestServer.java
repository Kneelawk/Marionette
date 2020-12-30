package com.kneelawk.mce2etest.server;

public class MCE2ETestServer {
    public static void main(String[] args) throws Throwable {
        MinecraftServerAccess access = new MinecraftServerAccess(args);
        access.start();
    }
}
