package com.rawstocktechnologies.portfoliomanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmeritradeInstrament {
    private AmeritradeFundamentals fundamental;
}
