package rs.edu.raf.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class OrderResponse {

    private String ticker;

    private Integer quantity;

    private BigDecimal limit;

    private BigDecimal stop;

    private boolean allOrNone;

    private boolean margin;

    private String action;

    private String type;

    private String status;

}
