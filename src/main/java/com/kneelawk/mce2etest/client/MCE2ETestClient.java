package com.kneelawk.mce2etest.client;

public class MCE2ETestClient {
    public static void main(String[] args) throws Throwable {
        MinecraftClientAccess access = new MinecraftClientAccess(args);
        access.start();
    }
}
