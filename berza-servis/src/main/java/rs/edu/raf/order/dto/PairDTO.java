package rs.edu.raf.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PairDTO {
    private Long userId;
    private BigDecimal valueChange;
}