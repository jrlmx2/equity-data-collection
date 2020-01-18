package com.rawstocktechnologies.portfoliomanager.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonDeserialize(using = CandleDeserialization.class)
public class Candle {
    private double low;
    private double high;
    private double open;
    private double close;
    private long volume;
    private Timestamp datetime;

    public Candle(double low, double high, double open, double close, long volume, Timestamp datetime){
        this.low=low;
        this.high=high;
        this.open=open;
        this.close=close;
        this.volume=volume;
        this.datetime=datetime;
    }
}
