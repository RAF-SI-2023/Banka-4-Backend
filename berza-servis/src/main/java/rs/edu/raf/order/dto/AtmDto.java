package rs.edu.raf.order.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AtmDto {
    private Long brojRacuna;
    private BigDecimal stanje;
}
