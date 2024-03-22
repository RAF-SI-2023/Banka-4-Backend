package rs.edu.raf.stocks.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class StockMonthlyResponse {
    @SerializedName("Meta Data")
    private Map<String, String> metaData;
    @SerializedName("Monthly Time Series")
    private Map<String, StockHistoryInfo> timeSeriesMonthly;
}
