package rs.edu.raf.model.entities.racun;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class ExchangeAccount {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;
    @NotNull
    private BigDecimal stanje;
    @NotBlank
    private String currency;
}
