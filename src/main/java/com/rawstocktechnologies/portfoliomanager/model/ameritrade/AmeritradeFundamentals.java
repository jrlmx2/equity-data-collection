package com.rawstocktechnologies.portfoliomanager.model.ameritrade;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AmeritradeFundamentals {
        private String symbol;
        private double high52;
        private double low52;
        private double dividendAmount;
        private double dividendYield;
        private String dividendDate;
        private double peRatio;
        private double pegRatio;
        private double pbRatio;
        private double prRatio;
        private double pcfRatio;
        private double grossMarginTTM;
        private double grossMarginMRQ;
        private double netProfitMarginTTM;
        private double netProfitMarginMRQ;
        private double operatingMarginTTM;
        private double operatingMarginMRQ;
        private double returnOnEquity;
        private double returnOnAssets;
        private double returnOnInvestment;
        private double quickRatio;
        private double currentRatio;
        private double interestCoverage;
        private double totalDebtToCapital;
        private double ltDebtToEquity;
        private double totalDebtToEquity;
        private double epsTTM;
        private double epsChangePercentTTM;
        private double epsChangeYear;
        private double epsChange;
        private double revChangeYear;
        private double revChangeTTM;
        private double revChangeIn;
        private double sharesOutstanding;
        private double marketCapFloat;
        private double marketCap;
        private double bookValuePerShare;
        private double shortIntToFloat;
        private double shortIntDayToCover;
        private double divGrowthRate3Year;
        private double dividendPayAmount;
        private String dividendPayDate;
        private double beta;
        private double vol1DayAvg;
        private double vol10DayAvg;
        private double vol3MonthAvg;
}
