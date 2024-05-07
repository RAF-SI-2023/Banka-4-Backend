package rs.edu.raf.model.dto.racun;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarzniRacunCreateDTO {

    private Long vlasnik;

    private String valuta;

    @Pattern(regexp = "^(STOCKS|FOREX|OPTIONS|FUTURES)$", message = "Invalid grupaHartija")
    private String grupaHartija;

    private Long brojRacuna;

    private BigDecimal maintenanceMargin;
}
