package com.rawstocktechnologies.portfoliomanager.model.strategy;


import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import java.time.LocalDate;

public class HibernateStrategyType extends AbstractSingleColumnStandardBasicType<StrategyDetails> {

    public static final HibernateStrategyType INSTANCE = new HibernateStrategyType();

    public HibernateStrategyType() {
        super(VarcharTypeDescriptor.INSTANCE, HibernateStrategyDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "StrategyDetails";
    }
}
