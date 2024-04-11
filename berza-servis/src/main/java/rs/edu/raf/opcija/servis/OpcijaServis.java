package rs.edu.raf.opcija.servis;

import rs.edu.raf.opcija.dto.NovaOpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaDto;
import rs.edu.raf.opcija.model.KorisnikoveKupljeneOpcije;
import rs.edu.raf.opcija.model.Opcija;
import rs.edu.raf.opcija.model.OpcijaStanje;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface OpcijaServis {

        List<OpcijaDto> findAll() throws InterruptedException;

        List<Opcija> findAl();


        OpcijaDto save(NovaOpcijaDto option);

        Opcija save(Opcija opcija);

        OpcijaDto findById(Long id);

        void azuirajPostojeceOpcije() throws IOException;

        List<OpcijaDto> findByStockAndDateAndStrike(String ticker, LocalDateTime datumIstekaVazenja, Double strikePrice);

        KorisnikoveKupljeneOpcije izvrsiOpciju(Long opcijaId, Long userId);

        OpcijaStanje proveriStanjeOpcije(Long opcijaId);

}
