package rs.edu.raf.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class gitPairDTO {
    private Long userId;
    private BigDecimal valueChange;
}