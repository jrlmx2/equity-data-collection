package com.rawstocktechnologies.portfoliomanager.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawstocktechnologies.portfoliomanager.dao.EquityDataRepository;
import com.rawstocktechnologies.portfoliomanager.dao.IEXSymbolCompanyRepository;
import com.rawstocktechnologies.portfoliomanager.model.Candle;
import com.rawstocktechnologies.portfoliomanager.model.DataIdentifier;
import com.rawstocktechnologies.portfoliomanager.model.EquityData;
import com.rawstocktechnologies.portfoliomanager.model.ameritrade.AmeritradeChart;
import com.rawstocktechnologies.portfoliomanager.model.iex.IEXSymbolCompany;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;


@Component
public class BiotechsThatPuked {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmeritradeDataCollection.class);

    @Value("${mail.destination}")
    private String toAddress;

    @Value("${biotech.lookback}")
    private Integer lookback;

    @Value("${biotech.percentageThreshold}")
    private Double threshold;

    @Autowired
    private AmeritradeAuth ameritradeAuth;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private EquityDataRepository dataRepository;

    @Autowired
    private IEXSymbolCompanyRepository iexSymbols;

    @Autowired
    private IEXDataCollection iex;

    private static final SimpleDateFormat emailDateFormatter = new SimpleDateFormat("MM/dd/yyyy");

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Comparator<Map<String,Object>> compareBiotechIdeas = new Comparator<Map<String,Object>>() {
        @Override
        public int compare(Map<String,Object> o1, Map<String,Object> o2) {
            double change1 = (double) o1.get("change");
            String symbol1 = (String) o1.get("symbol");

            double change2 = (double) o2.get("change");
            String symbol2 = (String) o2.get("symbol");

            if(change1 < change2)
                return -1;

            if(change2 < change1)
                return 1;

            return StringUtils.compare(symbol1,symbol2);
        }
    };

    @Scheduled(cron = "0 0 9 * * TUE/THU")
    public void findBiotectsThatPuked(){
        try {
            // Collect the ideas
            List<Map<String,Object>> ideas = new ArrayList<>();

            DataIdentifier id = ameritradeAuth.getID("AAPL");
            for(IEXSymbolCompany company : iexSymbols.findAll()){
                if(company == null || !org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase(company.getIndustry(),"biotechnology", "Pharmaceuticals: Major", "Pharmaceuticals: Other", "Pharmaceuticals: Generic") || !StringUtils.equalsIgnoreCase(company.getSector(), "Health Technology")) {
                    //LOGGER.warn("Skipping company {}  Sector: {} !== Healthcare || Industry: {} !== biotechnology, diagnostics & research",company.getCompanyName(), company.getSector(), company.getIndustry());
                    continue;
                }

                id.setSymbol(company.getSymbol());
                Optional<EquityData> results = dataRepository.findById(id);
                if(results.isEmpty())
                    continue;

                EquityData data = results.get();

                AmeritradeChart chart = mapper.readValue(data.getChart(), AmeritradeChart.class);
                int candleSize = chart.getCandles().size();
                if(candleSize < lookback + 1) {
                    LOGGER.warn("Skipping company {} because they do not have enough candles for the lookback period: {} < {}",company.getCompanyName(), candleSize, lookback);
                    continue;
                }

                int start = candleSize - (lookback+1);
                Candle startCandle = chart.getCandles().get(start);
                Candle endCandle = chart.getCandles().get(candleSize-1);

                try {
                    double change = (endCandle.getClose() - startCandle.getOpen()) / startCandle.getOpen();
                    LOGGER.info("{}: {} <= {}", company.getCompanyName(), change, threshold);
                    if (change <= threshold) {
                        Map<String, Object> ideaEntry = new HashMap<>();
                        ideaEntry.put("symbol", company.getSymbol());
                        ideaEntry.put("change", change);
                        ideas.add(ideaEntry);
                    }
                } catch (NullPointerException npe){
                    LOGGER.info("found null candle on symbol: {}",company.getSymbol());
                }

            }

            LOGGER.info("Sending emails with {} ideas",ideas.size());
            if(ideas.size() > 0) { // Only send the email if we found something worth showing
                // Sort the ideas by change, then alphabetically
                Collections.sort(ideas, compareBiotechIdeas);

                // Thymeleaf
                Map<String, Object> variables = new HashMap<>();
                variables.put("date", emailDateFormatter.format(new Date()));
                variables.put("ideas", ideas);

                Resource resource = resourceLoader.getResource("classpath:templates/biotechs-that-puked.html");
                InputStream input = resource.getInputStream();
                final IContext ctx = new Context(Locale.US, variables);
                String html = templateEngine.process(StreamUtils.copyToString(input, Charset.availableCharsets().get("UTF-8")), ctx);

                LOGGER.info("Email contents are {}",html);
                // send email
                sendEmail(toAddress, "Biotechs That Puked", html, null, true);
            }
        } catch (Exception ex) {
            LOGGER.error("Failed to find biotechs that puked with error ",ex);
        }
    }

    // more complicated example
    private void sendEmail(String to, String subject, String contents, File attachment, boolean isHtml) throws MessagingException, IOException {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();

            boolean multipart = false;
            if (attachment != null) {
                multipart = true;
            }
            // true = multipart message
            MimeMessageHelper helper = new MimeMessageHelper(msg, multipart);

            helper.setTo(to);

            helper.setSubject(subject);

            helper.setText(contents, isHtml);

            if (attachment != null) {
                FileSystemResource file = new FileSystemResource(attachment);
                helper.addAttachment(attachment.getName(), file);
            }

            javaMailSender.send(msg);
        } catch (Exception ex){
            LOGGER.error("Failed to send email with ",ex);
        }

    }
}
