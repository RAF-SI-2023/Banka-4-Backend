package rs.edu.raf.stocks.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class StockHistoryInfo {
    @SerializedName("1. open")
    private String open;
    @SerializedName("2. high")
    private String high;
    @SerializedName("3. low")
    private String low;
    @SerializedName("4. close")
    private String close;
    @SerializedName("5. volume")
    private String volume;
}
