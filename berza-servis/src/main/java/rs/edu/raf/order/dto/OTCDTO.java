package rs.edu.raf.order.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OTCDTO {
    private Long otcId;
    private String ticker;
    @Positive
    private Integer quantity;
}
