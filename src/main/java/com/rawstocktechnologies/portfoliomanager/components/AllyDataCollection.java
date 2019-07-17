package com.rawstocktechnologies.portfoliomanager.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawstocktechnologies.portfoliomanager.dao.EquityDataRepository;
import com.rawstocktechnologies.portfoliomanager.model.DataIdentifier;
import com.rawstocktechnologies.portfoliomanager.model.DataSource;
import com.rawstocktechnologies.portfoliomanager.model.EquityData;
import com.rawstocktechnologies.portfoliomanager.model.IEXSymbolRecord;
import com.rawstocktechnologies.portfoliomanager.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;

@Component
public class AllyDataCollection {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllyDataCollection.class);

    @Value("${datasource.ally.scheme}")
    private String iexScheme;

    @Value("${datasource.ally.version}")
    private String iexVersion;

    @Value("${datasource.ally.host}")
    private String iexHost;

    @Value("${filter.marketCapMinimum}")
    private long marketCapMinimum;

    @Autowired
    private EquityDataRepository equityData;

    @PostConstruct
    public void init() {
        LOGGER.info("Running once on startup with iex host {}",iexHost);
        collectAllyData();
    }

    private ObjectMapper mapper = JacksonUtils.mapper;

    public void collectAllyData() {
        LOGGER.info("Running data ally collection");

        String symbolsURL = buildIEXUrl("ref-data", "symbols");

        /*LOGGER.debug("Created url to grab symbols {}", symbolsURL);
        try {
            IEXSymbolRecord[] symbols = mapper.readValue(rest.getForObject(symbolsURL, String.class), IEXSymbolRecord[].class);
            int symbolCount = 0;
            for(IEXSymbolRecord symbol : symbols) {
                if(symbol.getIsEnabled() && StringUtils.equals(symbol.getCurrency(), "USD") && StringUtils.equals(symbol.getType(), "cs")){
                    try {
                        BigInteger marketCap = rest.getForObject(buildIEXUrl("stock", symbol.getSymbol(), "quote", "marketCap"), BigInteger.class);
                        if (marketCap.longValue() > marketCapMinimum) {
                            LOGGER.info("Found symbol {}: {}", symbol.getSymbol(), symbol.getExchange());
                            String chartUrl = buildIEXUrl("stock", symbol.getSymbol(), "chart", "1y");

                            EquityData data = new EquityData();
                            DataIdentifier id = new DataIdentifier();
                            id.setDataSource(DataSource.IEX);
                            id.setSymbol(symbol.getSymbol());
                            id.setDataSourceVersion(iexVersion);
                            data.setEquity(id);
                            data.setUrl(chartUrl);
                            data.setChart(rest.getForObject(chartUrl, String.class));
                            data.setStats("{}");
                            equityData.save(data);
                            symbolCount += 1;
                        }
                    } catch (Exception ex){
                        LOGGER.warn("Errored out trying to find the market cap for symbol {}", symbol.getSymbol());
                    }
                }
            }
            LOGGER.info("Found {} investigatable symbols", symbolCount);
        } catch (IOException e) {
            LOGGER.error("Could not fetch symbols with error ",e);
        }*/
    }

    private String buildIEXUrl(String... pathVars) {

        String path = iexVersion;
        if(pathVars.length > 0){
            path += "/"+StringUtils.join(pathVars,"/");
        }

        return new URIBuilder()
                .setScheme(iexScheme)
                .setHost(iexHost)
                .setPath(path).toString();
                //.addParameter("token", iexToken).toString();
    }
}
