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
public class MarzniRacun {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
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

    @NotNull
    private Boolean marginCall;

    private Long maintenanceDeadline;
}