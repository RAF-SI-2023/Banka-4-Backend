package rs.edu.raf.futures.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FuturesContractDto {
    private Long id;
    private String type;
    private String name;
    private BigDecimal price;
    private Integer contractSize;
    private String contractUnit;
    private BigDecimal openInterest;
    private Long settlementDate;
    private Double maintenanceMargin;
}
