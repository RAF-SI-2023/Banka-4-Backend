package rs.edu.raf.model.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealizacijaTransakcije {

    private Long brojRacuna;

    private Boolean aktivan;

    private BigDecimal rezervisanaSredstva;

    private String valute;

    private String tipRacuna;

    private BigDecimal prethodnoStanje;

    private Long idKorisnika;

}
