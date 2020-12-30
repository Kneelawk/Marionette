package com.kneelawk.mce2e;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class IOCopyThread extends Thread {
    private final InputStream is;
    private final OutputStream os;
    private final byte[] buffer = new byte[8192];

    public IOCopyThread(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
    }

    @Override
    public void run() {
        try {
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
