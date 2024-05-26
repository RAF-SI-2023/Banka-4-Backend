package rs.edu.raf.model.entities.racun;


import jakarta.persistence.*;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DevizniRacun extends Racun{
    public DevizniRacun(Long brojRacuna, Long vlasnik, BigDecimal stanje, BigDecimal raspolozivoStanje, Long zaposleni, Long datumKreiranja, Long datumIsteka, String currency, Boolean aktivan, BigDecimal kamatnaStopa, BigDecimal odrzavanjeRacuna) {
        super(brojRacuna, vlasnik, stanje, raspolozivoStanje, zaposleni, datumKreiranja, datumIsteka, currency, aktivan);
        this.kamatnaStopa = kamatnaStopa;
        this.odrzavanjeRacuna = odrzavanjeRacuna;
    }
    private BigDecimal kamatnaStopa; //procenat
    private BigDecimal odrzavanjeRacuna;
}
