package rs.edu.raf.model.entities.racun;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarzniRacun {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long vlasnik;

    @Column(unique = true)
    @NotNull
    private Long brojRacuna;

    @NotNull
    @NotBlank
    private String valuta;

    @NotNull
    @NotBlank
    private String grupaHartija;

    @NotNull
    private BigDecimal ulozenaSredstva;

    @NotNull
    private BigDecimal liquidCash;

    @NotNull
    private BigDecimal loanValue;

    @NotNull
    private BigDecimal maintenanceMargin;

    private Boolean marginCall;

    private Long maintenanceDeadline;
}