package com.rawstocktechnologies.portfoliomanager.model.ameritrade;

import com.rawstocktechnologies.portfoliomanager.model.Candle;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AmeritradeChart {
    private boolean empty;
    private String symbol;
    private List<Candle> candles = new ArrayList<>();
}
