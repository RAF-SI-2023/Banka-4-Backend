package rs.edu.raf.stocks.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class StockDailyResponse {
    @SerializedName("Meta Data")
    private Map<String, String> metaData;
    @SerializedName("Time Series (Daily)")
    private Map<String, StockHistoryInfo> timeSeriesDaily;
}
