package rs.edu.raf.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Banka3StockDTO {

    private Integer amount;

    private String ticker;
}
