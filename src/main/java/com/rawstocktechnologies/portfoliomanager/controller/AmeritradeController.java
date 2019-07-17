package com.rawstocktechnologies.portfoliomanager.controller;

import com.rawstocktechnologies.portfoliomanager.components.AmeritradeAuth;
import com.rawstocktechnologies.portfoliomanager.components.AmeritradeCredentials;
import com.rawstocktechnologies.portfoliomanager.components.AmeritradeDataCollection;
import com.rawstocktechnologies.portfoliomanager.components.IEXDataCollection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private RestTemplate rest = new RestTemplate();

    @GetMapping(path="/auth")
    @CrossOrigin
    public String getToken(HttpServletRequest request, HttpServletResponse response){
        LOGGER.info("request uri {} ",request.getRequestURL());
        if(StringUtils.isNotBlank(request.getParameter("code"))){
            auth.establishCredentials(request.getParameter("code"));
            data.collectAmeritradeData();
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
