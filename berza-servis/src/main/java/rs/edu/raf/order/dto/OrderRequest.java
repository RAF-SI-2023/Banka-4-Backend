package rs.edu.raf.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {

    private Long userId;

    private Long radnikId;

    private String ticker;

    private Integer quantity;

    private BigDecimal limit;

    private BigDecimal stop;

    private boolean allOrNone;

    private boolean margin;

    private String action;

}
