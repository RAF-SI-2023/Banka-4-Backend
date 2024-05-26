package rs.edu.raf.model.entities.racun;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class PravniRacun extends Racun {
    private String autorizovaniZaposleni = ""; // CSV foramt autorizovanih zaposlenih - ne koristi se

    public PravniRacun(Long brojRacuna, Long vlasnik, BigDecimal stanje, BigDecimal raspolozivoStanje, Long zaposleni, Long datumKreiranja, Long datumIsteka, String currency, Boolean aktivan) {
        super(brojRacuna, vlasnik, stanje, raspolozivoStanje, zaposleni, datumKreiranja, datumIsteka, currency, aktivan);
    }
}

