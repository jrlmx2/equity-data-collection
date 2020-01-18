package com.rawstocktechnologies.portfoliomanager.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawstocktechnologies.portfoliomanager.dao.EquityDataRepository;
import com.rawstocktechnologies.portfoliomanager.dao.IEXSymbolCompanyRepository;
import com.rawstocktechnologies.portfoliomanager.model.DataIdentifier;
import com.rawstocktechnologies.portfoliomanager.model.DataSource;
import com.rawstocktechnologies.portfoliomanager.model.EquityData;
import com.rawstocktechnologies.portfoliomanager.model.iex.IEXSymbolCompany;
import com.rawstocktechnologies.portfoliomanager.model.iex.IEXSymbolRecord;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class IEXDataCollection {
    private static final Logger LOGGER = LoggerFactory.getLogger(IEXDataCollection.class);

    @Value("${datasource.iex.scheme}")
    private String iexScheme;

    @Value("${datasource.iex.token}")
    private String iexToken;

    @Value("${datasource.iex.version}")
    private String iexVersion;

    @Value("${datasource.iex.host}")
    private String iexHost;

    @Value("${filter.marketCapMinimum}")
    private long marketCapMinimum;

    @Autowired
    private IEXSymbolCompanyRepository symbolRepo;

    private ObjectMapper mapper = new ObjectMapper();

    private RestTemplate rest = new RestTemplate();

    @Autowired
    private EquityDataRepository equityData;

    public IEXSymbolCompany getCompanyForSymbol(String symbol){
        try {
            return rest.getForEntity(buildIEXUrl("stock", symbol, "company"), IEXSymbolCompany.class).getBody();
        } catch (Exception ex) {
            LOGGER.error("failed to get IEX Comapny data for symbol",ex);
            return null;
        }
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Running once on startup with iex host {}",iexHost);
        //collectIEXData();
    }

    @Scheduled(cron="0 0 10 1 * *")
    public void updateSymbolList(){
        LOGGER.info("Running symbol update");

        String symbolsURL = buildIEXUrl("ref-data", "iex", "symbols");

        try {
            IEXSymbolRecord[] symbols = mapper.readValue(rest.getForObject(symbolsURL, String.class), IEXSymbolRecord[].class);
            int symbolCount = 0;
            List<String> symbolsStrings = new ArrayList<>();
            for (IEXSymbolRecord symbol : symbols) {
                if(BooleanUtils.isTrue(symbol.getIsEnabled()))
                    symbolsStrings.add(symbol.getSymbol());
            }

            List<IEXSymbolCompany> companiesToDelete = symbolRepo.findBySymbolNotIn(symbolsStrings.toArray(new String[0]));
            for(IEXSymbolCompany toDelete : companiesToDelete){
                symbolRepo.delete(toDelete);
            }

            for(String symbol : symbolsStrings){
                symbolRepo.save(getCompanyForSymbol(symbol));
            }

        } catch (Exception ex){
            LOGGER.error("Failed to update symbols with error ",ex);
        }

    }

    //@Scheduled(cron="0 0 5 28 FEB,MAY,AUG,DEC *")
    public void collectIEXData() {
        LOGGER.info("Running data collection");

        String symbolsURL = buildIEXUrl("ref-data", "symbols");

        LOGGER.debug("Created url to grab symbols {}", symbolsURL);
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
        }
    }

    private String buildIEXUrl(String... pathVars) {

        String path = iexVersion;
        if(pathVars.length > 0){
            path += "/"+StringUtils.join(pathVars,"/");
        }

        return new URIBuilder()
                .setScheme(iexScheme)
                .setHost(iexHost)
                .setPath(path)
                .addParameter("token", iexToken).toString();
    }
}
