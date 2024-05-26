package rs.edu.raf.model.entities.racun;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TekuciRacun extends Racun{

    public TekuciRacun(Long brojRacuna, Long vlasnik, BigDecimal stanje, BigDecimal raspolozivoStanje, Long zaposleni, Long datumKreiranja, Long datumIsteka, String currency, Boolean aktivan, String vrstaRacuna, BigDecimal kamatnaStopa, BigDecimal odrzavanjeRacuna) {
        super(brojRacuna, vlasnik, stanje, raspolozivoStanje, zaposleni, datumKreiranja, datumIsteka, currency, aktivan);
        this.vrstaRacuna = vrstaRacuna;
        this.kamatnaStopa = kamatnaStopa;
        this.odrzavanjeRacuna = odrzavanjeRacuna;
    }

    @NotBlank
    private String vrstaRacuna; //Poslovni, Lični, Štedni, Penzionerski, Devizni, Studentski

    @NotNull
    private BigDecimal kamatnaStopa; //procenat

    @NotNull
    private BigDecimal odrzavanjeRacuna;
}

