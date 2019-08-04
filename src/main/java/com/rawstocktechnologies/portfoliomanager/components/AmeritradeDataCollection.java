package com.rawstocktechnologies.portfoliomanager.components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawstocktechnologies.portfoliomanager.dao.EquityDataRepository;
import com.rawstocktechnologies.portfoliomanager.model.*;
import com.rawstocktechnologies.portfoliomanager.utils.EquityUtils;
import com.rawstocktechnologies.portfoliomanager.utils.JacksonUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class AmeritradeDataCollection {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmeritradeDataCollection.class);

    @Value("${filter.marketCapMinimum}")
    private long marketCapMinimum;

    @Autowired
    private AmeritradeAuth auth;

    @Autowired
    private EquityDataRepository equityData;

    private ObjectMapper mapper = JacksonUtils.mapper;

    @PostConstruct
    public void init() throws InterruptedException {
        LOGGER.info("Running once on startup with ameritrade host {}");
    }

    @Scheduled(cron="0 0 4 * * *")
    //@Scheduled(cron="0 0 1 28 FEB,MAY,AUG,DEC *")
    public void collectAmeritradeData() {

        // TODO fix fundamental unmarshalling.
        LOGGER.debug("Starting data collection process");
        long marketCapMinLimit = marketCapMinimum / 1000000;
        int symbolCount = 0;
        int iterationCount = 0;
        TypeReference<HashMap<String, AmeritradeInstrament>> typeRef
                = new TypeReference<HashMap<String, AmeritradeInstrament>>() {};
        for(String symbol : EquityUtils.symbols) {
            try {
                if(iterationCount % 5 == 0 || iterationCount == 0)
                    auth.establishCredentials(null);
                HttpURLConnection con = auth.buildRequest("GET", auth.buildUrl(new String[]{"instruments"}).addParameter("symbol",symbol).addParameter("projection","fundamental").toString());
                try {
                    if(con.getResponseCode()==429){
                        Thread.sleep(60000);
                        con.disconnect();
                        con = auth.buildRequest("GET", auth.buildUrl(new String[]{"instruments"}).addParameter("symbol",symbol).addParameter("projection","fundamental").toString());
                    }
                    if(con.getResponseCode()==401){
                        auth.establishCredentials(null);
                        con.disconnect();
                        con = auth.buildRequest("GET", auth.buildUrl(new String[]{"instruments"}).addParameter("symbol",symbol).addParameter("projection","fundamental").toString());
                    }
                    String fundamentals = StreamUtils.copyToString(con.getInputStream(), StandardCharsets.UTF_8);
                    HashMap<String, AmeritradeInstrament> funds = mapper.readValue(fundamentals, typeRef);
                    con.disconnect();
                    if (funds.containsKey(symbol)) {
                        LOGGER.info("Found potential symbol {}", symbol);

                        EquityData data = new EquityData();
                        DataIdentifier id = new DataIdentifier();
                        id.setDataSource(DataSource.AMERITRADE);
                        id.setSymbol(symbol);
                        id.setDataSourceVersion(auth.getVersion());
                        data.setEquity(id);

                        Range range = getRange(36);
                        URIBuilder chartUrlBuilder =  auth.buildUrl(new String[]{"marketdata",symbol,"pricehistory"})
                                .addParameter("periodType","month")
                                .addParameter("frequencyType","daily")
                                .addParameter("frequency","1")
                                .addParameter("startDate",Long.toString(range.getStart()))
                                .addParameter("endDate",Long.toString(range.getEnd()))
                                .addParameter("needExtendedHoursData","false");

                        HttpURLConnection conn = auth.buildRequest("GET",chartUrlBuilder.toString());
                        if(conn.getResponseCode()==429){
                            Thread.sleep(60000);
                            conn.disconnect();
                            conn = auth.buildRequest("GET",chartUrlBuilder.toString());
                        }
                        if(conn.getResponseCode()==401){
                            auth.establishCredentials(null);
                            conn.disconnect();
                            conn = auth.buildRequest("GET",chartUrlBuilder.toString());
                        }
                        data.setChart(StreamUtils.copyToString(conn.getInputStream(), StandardCharsets.UTF_8));
                        conn.disconnect();

                        data.setStats(mapper.writeValueAsString(funds.get(symbol).getFundamental()));
                        data.setUrl(chartUrlBuilder.toString());
                        data.setStats(JacksonUtils.mapper.writeValueAsString(funds.get(symbol).getFundamental()));
                        equityData.save(data);
                        symbolCount += 1;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            } catch (Exception ex) {
                LOGGER.warn("Errored out trying to find the market cap for symbol {}", symbol);
                ex.printStackTrace();
            }
        }
        LOGGER.info("Found {} investigatable symbols", symbolCount);
    }

    @Data
    private static class Range {
        private long start;
        private long end;
    }

    private Range getRange(int months){
        Range range = new Range();
        LocalDateTime now = LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1);

        int currentMonth = now.getMonthValue();
        while(currentMonth != now.getMonthValue()){
            now = now.minusDays(1);
        }

        range.setEnd(Timestamp.valueOf(now).getTime());
        range.setStart(Timestamp.valueOf(now.minusMonths(months)).getTime());

        return range;
    }


}
