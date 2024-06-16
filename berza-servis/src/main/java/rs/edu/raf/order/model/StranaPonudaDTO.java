package rs.edu.raf.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StranaPonudaDTO {

    private String ticker;

    private Integer quantity;

    private Double amountOffered;
}
