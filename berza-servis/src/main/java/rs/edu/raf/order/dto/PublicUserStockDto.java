package rs.edu.raf.order.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PublicUserStockDto {
    private Long stockId;
    private String ticker;
    private Integer quantity;
}
