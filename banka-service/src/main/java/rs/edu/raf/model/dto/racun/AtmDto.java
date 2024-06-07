package rs.edu.raf.model.dto.racun;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AtmDto {
    private Long brojRacuna;
    private BigDecimal stanje;
}
