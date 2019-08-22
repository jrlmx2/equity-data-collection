package com.rawstocktechnologies.portfoliomanager.controller;

import com.rawstocktechnologies.portfoliomanager.components.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

@RestController
@RequestMapping(path="/api/ameritrade")
public class AmeritradeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(IEXDataCollection.class);

    @Autowired
    AmeritradeAuth auth;

    @Autowired
    AmeritradeDataCollection data;

    @GetMapping(path="/chart")
    @CrossOrigin
    public String getChart(@QueryParam("symbol") String symbol, @QueryParam("months") Integer months) {
        if(symbol == null){
            symbol = "AAPL";
        }

        if(months == null) {
            months = 36;
        }
        LOGGER.info("Made it into the endpoint with symbol {} and months {}", symbol, months);
        return data.getChart(symbol, months);
    }

    @GetMapping(path="/auth")
    @CrossOrigin
    public String getToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        LOGGER.info("request uri {} ",request.getRequestURL());
        try {
            response.sendRedirect(auth.buildAuthUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Redirecting....";
    }
}
