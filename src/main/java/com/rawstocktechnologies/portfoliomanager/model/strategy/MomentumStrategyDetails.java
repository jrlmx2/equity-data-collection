package com.rawstocktechnologies.portfoliomanager.model.strategy;


import com.rawstocktechnologies.portfoliomanager.model.Position;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MomentumStrategyDetails implements StrategyDetails {

    private StrategyType type = StrategyType.QUANTMOMENTUM;

    private String holdDuration = "3m";

    private String lookback = "12m";

    private String lookbackMinus = "1m";

    private BigDecimal fipThreshold = new BigDecimal(1);

    private BigDecimal MomentumThreshold = new BigDecimal(1);

    // Ratio weight for momentum to fip
    private BigDecimal weightRatio = new BigDecimal(0.5);

    public boolean close(Position position) {

        return false;
    }

    public boolean open() {
        return false;
    }
}
