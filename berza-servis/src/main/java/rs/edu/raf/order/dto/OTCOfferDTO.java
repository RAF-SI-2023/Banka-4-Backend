package rs.edu.raf.order.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OTCOfferDTO {
    private Long stockId;
    private Long buyerId;
    private String ticker;
    @Positive
    private Integer quantity;
    private BigDecimal priceOffered;
}
