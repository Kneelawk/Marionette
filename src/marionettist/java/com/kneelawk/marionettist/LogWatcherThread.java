package com.kneelawk.marionettist;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LogWatcherThread extends Thread {
    private final InputStream is;
    private final OutputStream os;
    private final String prefix;
    private final List<String> errorMessages = new ArrayList<>();

    public LogWatcherThread(InputStream is, OutputStream os) {
        this(is, os, null);
    }

    public LogWatcherThread(InputStream is, OutputStream os, String prefix) {
        this.is = is;
        this.os = os;
        this.prefix = prefix;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(is);
        PrintStream out = new PrintStream(os);

        while (in.hasNextLine()) {
            String line = in.nextLine();
            if (line.startsWith("[FATAL]")) {
                errorMessages.add(line);
            }

            if (prefix != null) {
                out.println(prefix + line);
            } else {
                out.println(line);
            }

            out.flush();
        }

        out.close();
    }

    public void checkError() {
        if (errorMessages.size() == 1) {
            throw new LogErrorException(errorMessages.get(0));
        } else if (errorMessages.size() > 1) {
            StringBuilder message = new StringBuilder("Multiple error messages:\n");
            for (String error : errorMessages) {
                message.append("  ").append(error).append("\n");
            }
            throw new LogErrorException(message.toString());
        }
    }
}
