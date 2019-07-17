package com.rawstocktechnologies.portfoliomanager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class EquityUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquityUtils.class);

    public static String[] symbols;

    static {
        try {
            File file = ResourceUtils.getFile("classpath:symbols.json");
            InputStream in = new FileInputStream(file);
            symbols = JacksonUtils.mapper.readValue(in, String[].class);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }
}
