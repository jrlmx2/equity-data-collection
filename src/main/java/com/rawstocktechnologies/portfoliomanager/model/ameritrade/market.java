package com.rawstocktechnologies.portfoliomanager.model.ameritrade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class market {
    private String category;
    private String date;
    private String exchange;
    private String marketType;
    private String product;
    private String productName;
}
