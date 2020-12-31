package com.kneelawk.marionettist;

// TODO: Make this not a RuntimeException.
public class LogErrorException extends RuntimeException {
    public LogErrorException(String message) {
        super(message);
    }
}
