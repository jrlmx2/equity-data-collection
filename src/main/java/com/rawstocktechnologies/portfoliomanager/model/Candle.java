package com.rawstocktechnologies.portfoliomanager.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.sql.Timestamp;

@Data
@JsonDeserialize(using = CandleDeserialization.class)
public class Candle {
    private Double low;
    private Double high;
    private Double open;
    private Double close;
    private Integer volume;
    private Timestamp datetime;

    public Candle(Double low, Double high, Double open, Double close, Integer volume, Timestamp datetime){
        this.low=low;
        this.high=high;
        this.open=open;
        this.close=close;
        this.volume=volume;
        this.datetime=datetime;
    }
}
