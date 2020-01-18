package com.rawstocktechnologies.portfoliomanager.components;

import com.rawstocktechnologies.portfoliomanager.model.strategy.Strategy;
import com.rawstocktechnologies.portfoliomanager.utils.ProcessUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Momentum {
    private static final Logger LOGGER = LoggerFactory.getLogger(Momentum.class);

    /*@Autowired
    private StrategyRepository strateiges;*/

    @Scheduled(cron="0 0 7 1 MAR,JUN,SEP,DEC *")
    public void updateMomentum() throws IOException {
        LOGGER.info("Starting to process basic quant momentum");
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        try {
            LOGGER.info("Running Rust");
            if (isWindows) {
                ProcessUtils.processCommand("momentum", "c:\\\\work\\trading\\portfoliomanager\\src\\main\\resources\\bin\\rust-portfolio.exe", LOGGER);
            } else {
                ProcessUtils.processCommand("momentum", "c:\\\\work\\trading\\portfoliomanager\\src\\main\\resources\\bin\\rust-portfolio.exe", LOGGER);
            }

            Strategy strategy = null; //strateiges.findByName("Quantitative Momentum");


        } catch (Exception ex) {
            LOGGER.error("erred out updating momentum strategy with",ex);
            ex.printStackTrace();
        }
    }
}
