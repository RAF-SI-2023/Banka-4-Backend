package rs.edu.raf.model.entities.racun;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Racun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private Long brojRacuna;
    private Long vlasnik;
    private BigDecimal stanje;
    private BigDecimal raspolozivoStanje;
    private Long zaposleni;

    private Long datumKreiranja;

    private Long datumIsteka;
    private String currency;
    private Boolean aktivan;

    public Racun(Long brojRacuna, Long vlasnik, BigDecimal stanje, BigDecimal raspolozivoStanje, Long zaposleni,
                 Long datumKreiranja, Long datumIsteka, String currency, Boolean aktivan) {
        this.brojRacuna = brojRacuna;
        this.vlasnik = vlasnik;
        this.stanje = stanje;
        this.raspolozivoStanje = raspolozivoStanje;
        this.zaposleni = zaposleni;
        this.datumKreiranja = datumKreiranja;
        this.datumIsteka = datumIsteka;
        this.currency = currency;
        this.aktivan = aktivan;
    }
}
