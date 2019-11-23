package com.rawstocktechnologies.portfoliomanager.utils;

import lombok.Cleanup;
import org.slf4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;

public class ProcessUtils {

    public static void processCommand(String out, String cmd, Logger logger) throws IOException, InterruptedException {
        @Cleanup FileOutputStream fosLog;
        @Cleanup FileOutputStream fosError;
        try {
            fosLog = new FileOutputStream(out+"log");
            fosError = new FileOutputStream(out+"Error");
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            // any error message?
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", fosError, logger);

            // any output?
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", fosLog, logger);

            // kick them off
            errorGobbler.start();
            outputGobbler.start();

            // any error???
            int exitVal = proc.waitFor();
            if(exitVal != 0){
                throw new IllegalStateException("Failed to run command: "+cmd+" please check logs.");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }

}
