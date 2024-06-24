package rs.edu.raf.order.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OTCPlaceDTO {
    private Long userId;
    private String ticker;
    @Positive
    private Integer quantity;
}
