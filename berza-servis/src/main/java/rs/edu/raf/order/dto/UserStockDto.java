package rs.edu.raf.order.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserStockDto {

    private Long id;
    private String ticker;
    private Integer quantity;
    private Integer publicQuantity;
    private BigDecimal currentBid;
    private BigDecimal currentAsk;

}
