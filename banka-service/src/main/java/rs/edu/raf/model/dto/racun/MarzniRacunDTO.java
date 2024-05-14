package rs.edu.raf.model.dto.racun;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarzniRacunDTO {

    private Long vlasnik;

    private Long brojRacuna;

    private String valuta;

    private String grupaHartija;

    private BigDecimal ulozenaSredstva;

    private BigDecimal loanValue;

    private BigDecimal maintenanceMargin;

    private BigDecimal liquidCash;

    private Boolean marginCall;

}
