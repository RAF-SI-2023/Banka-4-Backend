package rs.edu.raf.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PonudaDTO {

    private Long stockID;

    private Integer quantity;

    private BigDecimal amountOffered;
}
