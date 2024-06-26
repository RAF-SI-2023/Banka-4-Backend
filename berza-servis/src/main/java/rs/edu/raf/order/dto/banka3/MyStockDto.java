package rs.edu.raf.order.dto.banka3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyStockDto implements Serializable {
    private Integer amount;
    private String ticker;
}
