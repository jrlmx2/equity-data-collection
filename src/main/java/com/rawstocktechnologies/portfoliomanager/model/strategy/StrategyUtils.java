package com.rawstocktechnologies.portfoliomanager.model.strategy;

import java.lang.reflect.Type;

public class StrategyUtils {
    public static Class unmarshallTo(StrategyType type) {
        switch(type){
            default:
                return MomentumStrategyDetails.class;
        }
    }
}
