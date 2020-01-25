package com.rawstocktechnologies.portfoliomanager.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.LongNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;

public class CandleDeserialization extends StdDeserializer<Candle> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CandleDeserialization.class);

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
        //LOGGER.info("deserialing node low: {}, high: {}, open: {}, close: {}, volume: {}, datetime: {}",node.get("low"), node.get("high"), node.get("open"), node.get("close"), node.get("volume"), node.get("datetime"));
        Double low = null;
        if(StringUtils.isNotBlank(node.get("low").asText()) && !StringUtils.equals(node.get("low").asText(), "NaN"))
            low = Double.parseDouble(node.get("low").asText());

        Double high = null;
        if(StringUtils.isNotBlank(node.get("high").asText()) && !StringUtils.equals(node.get("high").asText(), "NaN"))
            high = Double.parseDouble(node.get("high").asText());

        Double open = null;
        if(StringUtils.isNotBlank(node.get("open").asText()) && !StringUtils.equals(node.get("open").asText(), "NaN"))
            open = Double.parseDouble(node.get("open").asText());

        Double close = null;
        if(StringUtils.isNotBlank(node.get("close").asText()) && !StringUtils.equals(node.get("close").asText(), "NaN"))
            close = Double.parseDouble(node.get("close").asText());

        Integer volume = null;
        if(StringUtils.isNotBlank(node.get("volume").asText()) && !StringUtils.equals(node.get("volume").asText(), "NaN"))
            volume = Integer.parseInt(node.get("volume").asText());

        Timestamp datetime = new Timestamp(((long) ((LongNode) node.get("datetime")).numberValue()));

        return new Candle(low, high, open, close, volume, datetime);
    }
}
