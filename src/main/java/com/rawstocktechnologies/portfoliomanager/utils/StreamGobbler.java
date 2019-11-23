package com.rawstocktechnologies.portfoliomanager.utils;

import java.io.*;
import org.slf4j.Logger;

class StreamGobbler extends Thread {
    private InputStream is;
    private String type;
    private OutputStream os;
    private Logger logger;

    StreamGobbler(InputStream is, String type) {
        this(is, type, null, null);
    }

    StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    StreamGobbler(InputStream is, String type, Logger redirect) {
        this.is = is;
        this.type = type;
        this.logger = redirect;
    }

    StreamGobbler(InputStream is, String type, OutputStream os, Logger redirect) {
        this.is = is;
        this.type = type;
        this.os = os;
        this.logger = redirect;
    }

    public void run() {
        try {
            PrintWriter pw = null;
            if (os != null)
                pw = new PrintWriter(os);

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);

                if (logger != null) {
                    switch(type) {
                        case "ERROR":
                            logger.error(line);
                            break;
                        case "WARN":
                            logger.warn(line);
                            break;
                        default:
                            logger.info(line);
                            break;
                    }
                }
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}