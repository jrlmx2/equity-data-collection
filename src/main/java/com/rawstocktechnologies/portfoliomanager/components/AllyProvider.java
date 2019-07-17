package com.rawstocktechnologies.portfoliomanager.components;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.builder.api.OAuth1SignatureType;
import com.github.scribejava.core.model.Token;

public class AllyProvider extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://developers.tradeking.com/oauth/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_RESOURCE = "https://developers.tradeking.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "https://developers.tradeking.com/oauth/access_token";

    protected AllyProvider(){}

    @Override
    public String getAccessTokenEndpoint()
    {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    protected String getAuthorizationBaseUrl() {
        return AUTHORIZE_URL;
    }

    @Override
    public String getRequestTokenEndpoint()
    {
        return REQUEST_TOKEN_RESOURCE;
    }

    private static class InstanceHolder {
        private static final AllyProvider INSTANCE = new AllyProvider();
    }

    public static AllyProvider instance() {
        return InstanceHolder.INSTANCE;
    }
}
