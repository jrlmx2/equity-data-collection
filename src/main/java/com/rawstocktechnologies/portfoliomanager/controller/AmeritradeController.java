package com.rawstocktechnologies.portfoliomanager.controller;

import com.rawstocktechnologies.portfoliomanager.components.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Controller
@RequestMapping(path="/ameritrade")
public class AmeritradeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IEXDataCollection.class);

    @Autowired
    AmeritradeAuth auth;

    @Autowired
    AmeritradeDataCollection data;

    @Autowired
    Momentum momentum;

    @Autowired
    ThreadPoolTaskExecutor threading;


        private RestTemplate rest = new RestTemplate();

    @GetMapping(path="/auth")
    @CrossOrigin
    public String getToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        LOGGER.info("request uri {} ",request.getRequestURL());
        if(StringUtils.isNotBlank(request.getParameter("code"))){
            auth.establishCredentials(request.getParameter("code"));
            threading.execute(new Runnable() {
                @Override
                public void run() {
                    data.collectAmeritradeData();
                    try {
                        momentum.updateMomentum();
                    } catch (IOException ioe) {
                        LOGGER.error("Failed to run the momentum scoring system {}", ioe);
                    }
                }
            });
            return "<h1>Success<h1/>";
        }

        try {
            response.sendRedirect(auth.buildAuthUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Redirecting....";
    }
}
