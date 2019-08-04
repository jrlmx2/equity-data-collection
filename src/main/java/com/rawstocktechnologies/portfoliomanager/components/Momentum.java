package com.rawstocktechnologies.portfoliomanager.components;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Momentum {

    @Scheduled(cron="0 0 7 1 MAR,JUN,SEP,DEC *")
    public void updateMomentum() throws IOException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        Process process;
        if (isWindows) {
            process = Runtime.getRuntime()
                    .exec("c:\\\\work\\trading\\portfoliomanager\\src\\main\\resources\\bin\\rust-portfolio.exe");
        } else {
            process = Runtime.getRuntime()
                    .exec(String.format("sh -c ls %s", "~"));
        }
    }
}
