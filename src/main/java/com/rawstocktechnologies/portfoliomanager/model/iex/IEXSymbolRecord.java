package com.rawstocktechnologies.portfoliomanager.model.iex;

import lombok.Data;

@Data
public class IEXSymbolRecord {
    private String symbol;
    private String exchange;
    private String name;
    private String date;
    private String type;
    private String iexId;
    private String region;
    private String currency;
    private Boolean isEnabled;
}
