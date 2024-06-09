package rs.edu.raf.opcija.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OpcijaKorisnikaDto {

    private Long korisnikId;

    private Long opcijaId;

    private Long akcijaId;

    private BigDecimal akcijaTickerCenaPrilikomIskoriscenja;//stvarna cena akcija prilikom iskoriscenja opcije

}
