package rs.edu.raf.stocks.response;

import lombok.Data;

@Data
public class CompanyOverviewResponse {

        private String Symbol;
        private String AssetType;
        private String Name;
        private String Description;
        private String CIK;
        private String Exchange;
        private String Currency;
        private String Country;
        private String Sector;
        private String Industry;
        private String Address;
        private String FiscalYearEnd;
        private String LatestQuarter;
        private String MarketCapitalization;
        private String EBITDA;
        private String PERatio;
        private String PEGRatio;
        private String BookValue;
        private String DividendPerShare;
        private String DividendYield;
        private String EPS;
        private String RevenuePerShareTTM;
        private String ProfitMargin;
        private String OperatingMarginTTM;
        private String ReturnOnAssetsTTM;
        private String ReturnOnEquityTTM;
        private String RevenueTTM;
        private String GrossProfitTTM;
        private String DilutedEPSTTM;
        private String QuarterlyEarningsGrowthYOY;
        private String QuarterlyRevenueGrowthYOY;
        private String AnalystTargetPrice;
        private String AnalystRatingStrongBuy;
        private String AnalystRatingBuy;
        private String AnalystRatingHold;
        private String AnalystRatingSell;
        private String TrailingPE;
        private String ForwardPE;
        private String PriceToSalesRatioTTM;
        private String PriceToBookRatio;
        private String EVToRevenue;
        private String EVToEBITDA;
        private String Beta;
        private String WeekHigh52;
        private String WeekLow52;
        private String DayMovingAverage50;
        private String DayMovingAverage200;
        private String SharesOutstanding;
        private String DividentDate;
        private String ExDividendDate;
}
