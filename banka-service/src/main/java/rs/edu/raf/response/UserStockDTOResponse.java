package rs.edu.raf.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserStockDTOResponse {
    //mozda zatreba za svako od ovih polja
    //@SerializedName("ticker")
    private String ticker;
    private Integer quantity;
    private BigDecimal currentBid;
    private BigDecimal currentAsk;
}
