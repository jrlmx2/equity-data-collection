package com.rawstocktechnologies.portfoliomanager.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

public class CandleDeserialization extends StdDeserializer<Candle> {
    public CandleDeserialization() {
        this(null);
    }

    public CandleDeserialization(Class<?> vc) {
        super(vc);
    }


    @Override
    public Candle deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        double low = (double) ((DoubleNode) node.get("low")).numberValue();
        double high = (double) ((DoubleNode) node.get("high")).numberValue();
        double open = (double) ((DoubleNode) node.get("open")).numberValue();
        double close = (double) ((DoubleNode) node.get("close")).numberValue();
        long volume = (long) ((LongNode) node.get("volume")).numberValue();
        Timestamp datetime = new Timestamp(((long) ((LongNode) node.get("datetime")).numberValue()));

        return new Candle(low, high, open, close, volume, datetime);
    }
}
