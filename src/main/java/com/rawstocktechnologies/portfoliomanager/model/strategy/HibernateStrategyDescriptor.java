package com.rawstocktechnologies.portfoliomanager.model.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HibernateStrategyDescriptor extends AbstractTypeDescriptor<StrategyDetails> {

    private static final Logger LOGGER = LoggerFactory.getLogger(HibernateStrategyDescriptor.class);

    public static final ObjectMapper mapper = new ObjectMapper();

    public static final HibernateStrategyDescriptor INSTANCE =
            new HibernateStrategyDescriptor();

    public HibernateStrategyDescriptor() {
        super(StrategyDetails.class, ImmutableMutabilityPlan.INSTANCE);
    }

    @Override
    public String toString(StrategyDetails value) {
        try{
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to convert details to string ", e);
        }
        return null;
    }

    @Override
    public StrategyDetails fromString(String string) {
        try {
            TempStartegyDetailsContainer container = mapper.readValue(string, TempStartegyDetailsContainer.class);
            return (StrategyDetails) mapper.readValue(string, StrategyUtils.unmarshallTo(container.getType()));
        } catch (IOException e) {
            LOGGER.error("Failed to parse "+string, e);
        }
        return null;
    }

    @Override
    public <X> X unwrap(StrategyDetails value, Class<X> type, WrapperOptions options) {

        if (value == null)
            return null;

        if (String.class.isAssignableFrom(type))
            return (X) toString(value);

        throw unknownUnwrap(type);
    }

    @Override
    public <X> StrategyDetails wrap(X value, WrapperOptions options) {
        if(value == null)
            return null;

        if(String.class.isInstance(value)){
            return fromString((String) value);
        }

        throw unknownWrap(value.getClass());
    }
}
