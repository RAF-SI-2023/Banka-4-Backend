package rs.edu.raf.model.dto.racun;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarzniRacunUpdateDTO {

    private Long userId;

    @Pattern(regexp = "^(STOCKS|FOREX|OPTIONS|FUTURES)$", message = "Invalid grupaHartija")
    private String grupaHartija;

    private BigDecimal amount;

}
