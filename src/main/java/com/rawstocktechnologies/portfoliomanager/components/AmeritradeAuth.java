package com.rawstocktechnologies.portfoliomanager.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rawstocktechnologies.portfoliomanager.utils.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpConnection;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class AmeritradeAuth {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmeritradeDataCollection.class);

    @Value("${datasource.ameritrade.client_id}")
    private String clientID;

    @Value("${datasource.ameritrade.redirect_uri}")
    private String redirectURI;

    @Value("${datasource.ameritrade.scheme}")
    private String scheme;

    @Value("${datasource.ameritrade.base}")
    private String baseURL;

    @Value("${datasource.ameritrade.version}")
    private String version;

    private AmeritradeCredentials creds;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private ObjectMapper mapper = JacksonUtils.mapper;
    private RestTemplate rest = new RestTemplate();

    @Scheduled(cron="0 30 * * * *")
    public void testAccessToken(){
        if(creds == null)
            LOGGER.warn("Credentials test failed because it was never set. LOGIN");

        HttpURLConnection con = buildRequest("GET", buildUrl(new String[]{"marketdata", "EQUITY", "hours"}).toString());
        try {
            con.connect();
            if(con.getResponseCode() == 200){
                LOGGER.info("Access Token is still good!");
            } else {
                establishCredentials(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }
    };

    // TODO fix token refresh, it doesn't work when unauthorizerd
    public void establishCredentials(String code){
        HttpURLConnection con = buildRequest("POST",buildUrl(new String[] {"oauth2","token"}).toString(), false);
        con.setDoOutput(true);
        con.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty( "charset", "utf-8");
        con.setUseCaches( false );
        String params = "client_id="+clientID+"&access_type=offline&";
        if (code == null){ // Refresh
               params += "grant_type=refresh_token&refresh_token="+URLEncoder.encode(creds.getRefreshToken(),StandardCharsets.UTF_8);
        } else {
            params += "grant_type=authorization_code&code="+URLEncoder.encode(code,StandardCharsets.UTF_8);
        }
        params += "&redirect_uri="+URLEncoder.encode(redirectURI,StandardCharsets.UTF_8);

        con.setRequestProperty( "Content-Length", Integer.toString( params.length() ));
        try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
            wr.write( params.getBytes() );

            this.creds = mapper.readValue(con.getInputStream(), AmeritradeCredentials.class);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String buildAuthUrl() {
        return new URIBuilder()
                .setScheme(scheme)
                .setHost("auth.tdameritrade.com")
                .setPath("auth")
                .addParameter("client_id", clientID+"@AMER.OAUTHAP")
                .addParameter("response_type", "code")
                .addParameter("redirect_uri", redirectURI).toString();
    }

    public HttpURLConnection buildRequest(String method, String rawUrl){
        return buildRequest(method, rawUrl, true);
    }

    private HttpURLConnection buildRequest(String method, String rawUrl, boolean withCreds){
        HttpURLConnection con;
        try {
            URL url = new URL(rawUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            if(creds != null && withCreds)
                con.setRequestProperty("Authorization", "Bearer " + creds.getAccessToken());
            return con;
        }catch (MalformedURLException ex){
            LOGGER.error("Failed to build td ameritrade url {}: {}",method, rawUrl);
            ex.printStackTrace();
        } catch (ProtocolException e) {
            LOGGER.error("Failed to establish connection with td ameritrade url {}: {}",method, rawUrl);
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("Failed to read td ameritrade response from {}: {}",method, rawUrl);
            e.printStackTrace();
        }
        return null;
    }

    public URIBuilder buildUrl(String[] pathVars) {
        String path = version;
        if(pathVars.length > 0){
            path += "/"+StringUtils.join(pathVars,"/");
        }

        if(creds == null && !StringUtils.contains(path,"token")) {
            LOGGER.info("No credential information stored");
            return null;
        }

        return new URIBuilder()
                .setScheme(scheme)
                .setHost(baseURL)
                .setPath(path);
    }


    public String getVersion(){
        return version;
    }

}
