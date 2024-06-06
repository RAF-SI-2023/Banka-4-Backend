package rs.edu.raf.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StranaPonudaDTO {

    String ticker;

    private Integer quantity;

    private BigDecimal amountOffered;
}
