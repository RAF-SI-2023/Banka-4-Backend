package rs.edu.raf.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MojePonudeBanci3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;
    private Integer quantity;
    private Double amountOffered;
    private String status;

    public MojePonudeBanci3(String ticker, Integer quantity, Double amountOffered, String status) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.amountOffered = amountOffered;
        this.status = status;
    }
}
