package com.rawstocktechnologies.portfoliomanager.components;

import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class AllyClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(AllyClient.class);

    @Value("${datasource.ally.key}")
    private static String CONSUMER_KEY;

    @Value("${datasource.ally.secret}")
    private static String CONSUMER_SECRET;

    @Value("${datasource.ally.token")
    private static String OAUTH_TOKEN;

    @Value("${datasource.ally.token-secret}")
    private static String OAUTH_TOKEN_SECRET;

    private static final String PROTECTED_RESOURCE_URL = "https://api.tradeking.com/v1/member/profile.json";

    public static Response AllyRequest(String url, Verb method)
    {
        final OAuth10aService service = new ServiceBuilder(CONSUMER_KEY)
                .apiSecret(CONSUMER_SECRET)
                .build(AllyProvider.instance());
        try {
            OAuth1AccessToken accessToken = new OAuth1AccessToken(OAUTH_TOKEN, OAUTH_TOKEN_SECRET);

            final OAuthRequest request = new OAuthRequest(method, url);
            service.signRequest(accessToken, request); // the access token from step 4
            final Response response = service.execute(request);
            LOGGER.info("Got raw ally body {}",response.getBody());

            return response;
        } catch (IOException e) {
            LOGGER.error("Failed to pull in body from ally request "+url, e);
        } catch (InterruptedException e) {
            LOGGER.error("Request was interupted at url "+url, e);
        } catch (ExecutionException e) {
            LOGGER.error("Failed to request ally "+url, e);
        } catch (Exception e) {
            LOGGER.error("Unknown exception retrieving "+url, e);
        }

        return null;
    }
}
