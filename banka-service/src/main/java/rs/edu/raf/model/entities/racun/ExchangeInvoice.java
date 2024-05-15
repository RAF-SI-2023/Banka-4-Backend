package rs.edu.raf.model.entities.racun;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExchangeInvoice {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;
    private String senderAccount;
    private String toAccount;
    private BigDecimal senderAmount;
    private String senderCurrency;
    private String toCurrency;
    private BigDecimal exchangeRate;
    private BigDecimal profit;
    private Long dateAndTime;
    
}

