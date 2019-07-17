package com.rawstocktechnologies.portfoliomanager.components;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AmeritradeCredentials {
    @JsonAlias({"access_token"})
    private String accessToken;

    @JsonAlias({"refresh_token"})
    private String refreshToken;

    @JsonAlias({"token_type"})
    private String tokenType;

    @JsonAlias({"expires_in"})
    private long expiresIn;

    private String scope;

    @JsonAlias({"refresh_token_expires_in"})
    private long refreshTokenExpiresIn;
}
