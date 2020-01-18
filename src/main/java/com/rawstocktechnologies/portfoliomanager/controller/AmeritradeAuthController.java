package com.rawstocktechnologies.portfoliomanager.controller;

import com.rawstocktechnologies.portfoliomanager.components.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(path="/ameritrade")
public class AmeritradeAuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IEXDataCollection.class);

    @Autowired
    AmeritradeAuth auth;

    @Autowired
    AmeritradeDataCollection data;

    @Autowired
    Momentum momentum;

    @Autowired
    ThreadPoolTaskExecutor threading;

    @Autowired
    BiotechsThatPuked btp;

    @Autowired
    IEXDataCollection iex;

    @GetMapping(path="/auth")
    @CrossOrigin
    public void getToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(StringUtils.isNotBlank(request.getParameter("code"))){
            auth.establishCredentials(request.getParameter("code"));
            threading.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        iex.updateSymbolList();
                        data.collectAmeritradeData();
                        btp.findBiotectsThatPuked();
                        momentum.updateMomentum();

                    } catch (IOException ioe) {
                        LOGGER.error("Failed to run the momentum scoring system {}", ioe);
                    }
                }
            });
            response.sendRedirect("/");
            return;
        }

        try {
            response.sendRedirect(auth.buildAuthUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
