package rs.edu.raf.stocks.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class APILimitReachResponse {

    @SerializedName("Information")
    private String information;
}
