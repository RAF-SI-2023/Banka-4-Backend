package rs.edu.raf.opcija.model;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class KorisnikoveKupljeneOpcije {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long korisnikId;

    private Long opcijaId;

    private Long akcijaId;

    private BigDecimal akcijaTickerCenaPrilikomIskoriscenja;//stvarna cena akcija prilikom iskoriscenja opcije

    private boolean iskoriscena;//bilo da je put ili call iskorisceno
}