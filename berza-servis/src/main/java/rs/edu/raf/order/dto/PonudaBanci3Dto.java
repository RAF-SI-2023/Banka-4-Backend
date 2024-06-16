package rs.edu.raf.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PonudaBanci3Dto {
    private String ticker;
    private Integer amount;
    private Double price;
    private Long idBank4;
}
