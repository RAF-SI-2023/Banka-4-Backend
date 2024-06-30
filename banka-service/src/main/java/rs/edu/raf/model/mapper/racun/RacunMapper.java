package rs.edu.raf.model.mapper.racun;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.edu.raf.model.dto.racun.NoviDevizniRacunDTO;
import rs.edu.raf.model.dto.racun.NoviPravniRacunDTO;
import rs.edu.raf.model.dto.racun.NoviTekuciRacunDTO;
import rs.edu.raf.model.dto.racun.RacunDTO;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.repository.transaction.DevizniRacunRepository;
import rs.edu.raf.repository.transaction.PravniRacunRepository;
import rs.edu.raf.repository.transaction.TekuciRacunRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RacunMapper {

    private DevizniRacunRepository devizniRacunRepository;
    private PravniRacunRepository pravniRacunRepository;
    private TekuciRacunRepository tekuciRacunRepository;

    private Map<String, Integer> vrsteRacuna;

    @Autowired
    public RacunMapper(DevizniRacunRepository devizniRacunRepository, PravniRacunRepository pravniRacunRepository, TekuciRacunRepository tekuciRacunRepository) {
        this.devizniRacunRepository = devizniRacunRepository;
        this.pravniRacunRepository = pravniRacunRepository;
        this.tekuciRacunRepository = tekuciRacunRepository;
        initialiseVrste();
    }

    public DevizniRacun noviDevizniRacunDTOToDevizniRacun(NoviDevizniRacunDTO noviDevizniRacunDTO) {
        DevizniRacun dr = new DevizniRacun();

        Long id = devizniRacunRepository.findTop1ByOrderByIdDesc() * 100L;
        dr.setBrojRacuna(444000000000000011L + id); //444 sifra banke 0000 filial

        dr.setVlasnik(noviDevizniRacunDTO.getVlasnik());
        dr.setStanje(new BigDecimal("0"));
        dr.setRaspolozivoStanje(new BigDecimal("0"));
        dr.setZaposleni(noviDevizniRacunDTO.getZaposleni());
        dr.setDatumKreiranja(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000L);
        dr.setDatumIsteka(dr.getDatumKreiranja() + 5*1000L*31536000L);

        List<String> nazivi = noviDevizniRacunDTO.getCurrency();
        String valute = "";
        for (String naziv : nazivi) {
            valute = valute.concat(naziv + ",");
        }
        valute = valute.substring(0, valute.length() - 1);
        dr.setCurrency(valute);

        dr.setAktivan(true);
        dr.setKamatnaStopa(new BigDecimal("1"));
        dr.setOdrzavanjeRacuna(new BigDecimal(100 * noviDevizniRacunDTO.getBrojDozvoljenihValuta()));

        return dr;
    }

    public PravniRacun noviPravniRacunDTOToPravniRacun(NoviPravniRacunDTO noviPravniRacunDTO) {
        PravniRacun pr = new PravniRacun();

        Long id = pravniRacunRepository.findTop1ByOrderByIdDesc() * 100L;
        pr.setBrojRacuna(444000000000000022L + id); //444 sifra banke 0000 filial

        pr.setVlasnik(noviPravniRacunDTO.getFirma());
        pr.setStanje(new BigDecimal("0"));
        pr.setRaspolozivoStanje(new BigDecimal("0"));
        pr.setZaposleni(noviPravniRacunDTO.getZaposleni());
        pr.setDatumKreiranja(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000L);
        pr.setDatumIsteka(pr.getDatumKreiranja() + 5*1000L*31536000L);
        pr.setCurrency("RSD");
        pr.setAktivan(true);

        return pr;
    }

    public TekuciRacun noviTekuciRacunDTOToTekuciRacun(NoviTekuciRacunDTO noviTekuciRacunDTO) {
        TekuciRacun tr = new TekuciRacun();

        Long id = tekuciRacunRepository.findTop1ByOrderByIdDesc() * 100L;
        tr.setBrojRacuna(444000000000000033L + id); //444 sifra banke 0000 filial

        tr.setVlasnik(noviTekuciRacunDTO.getVlasnik());
        tr.setStanje(new BigDecimal("0"));
        tr.setRaspolozivoStanje(new BigDecimal("0"));
        tr.setZaposleni(noviTekuciRacunDTO.getZaposleni());
        tr.setDatumKreiranja(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000L);
        tr.setDatumIsteka(tr.getDatumKreiranja() + 5*1000L*31536000L);
        tr.setCurrency("RSD");
        tr.setAktivan(true);
        tr.setVrstaRacuna(noviTekuciRacunDTO.getVrstaRacuna());

        if (tr.getVrstaRacuna().equals("Penzionerski") || tr.getVrstaRacuna().equals("Studentski")) {
            tr.setKamatnaStopa(new BigDecimal("0.5"));
        } else {
            tr.setKamatnaStopa(new BigDecimal("0"));
        }

        tr.setOdrzavanjeRacuna(new BigDecimal(100 * vrsteRacuna.get(tr.getVrstaRacuna())));

        return tr;
    }

    public RacunDTO devizniRacunToRacunDTO(DevizniRacun dr) {
        RacunDTO dto = new RacunDTO();
        dto.setId(dr.getId());
        dto.setBrojRacuna(dr.getBrojRacuna().toString());
        dto.setVlasnik(dr.getVlasnik());
        dto.setStanje(dr.getStanje());
        dto.setRaspolozivoStanje(dr.getRaspolozivoStanje());
        dto.setZaposleni(dr.getZaposleni());
        dto.setDatumKreiranja(dr.getDatumKreiranja());
        dto.setDatumIsteka(dr.getDatumIsteka());
        dto.setCurrency(dr.getCurrency());
        dto.setAktivan(dr.getAktivan());
        return dto;
    }

    public RacunDTO pravniRacunToRacunDTO(PravniRacun pr) {
        RacunDTO dto = new RacunDTO();
        dto.setId(pr.getId());
        dto.setBrojRacuna(pr.getBrojRacuna().toString());
        dto.setVlasnik(pr.getVlasnik());
        dto.setStanje(pr.getStanje());
        dto.setRaspolozivoStanje(pr.getRaspolozivoStanje());
        dto.setZaposleni(pr.getZaposleni());
        dto.setDatumKreiranja(pr.getDatumKreiranja());
        dto.setDatumIsteka(pr.getDatumIsteka());
        dto.setCurrency(pr.getCurrency());
        dto.setAktivan(pr.getAktivan());
        return dto;
    }

    public RacunDTO tekuciRacunToRacunDTO(TekuciRacun tr) {
        RacunDTO dto = new RacunDTO();
        dto.setId(tr.getId());
        dto.setBrojRacuna(tr.getBrojRacuna().toString());
        dto.setVlasnik(tr.getVlasnik());
        dto.setStanje(tr.getStanje());
        dto.setRaspolozivoStanje(tr.getRaspolozivoStanje());
        dto.setZaposleni(tr.getZaposleni());
        dto.setDatumKreiranja(tr.getDatumKreiranja());
        dto.setDatumIsteka(tr.getDatumIsteka());
        dto.setCurrency(tr.getCurrency());
        dto.setAktivan(tr.getAktivan());
        return dto;
    }

    private void initialiseVrste() {
        vrsteRacuna = new HashMap<>();
        vrsteRacuna.put("Poslovni", 5);
        vrsteRacuna.put("Licni", 3);
        vrsteRacuna.put("Stedni", 2);
        vrsteRacuna.put("Penzionerski", 2);
        vrsteRacuna.put("Devizni", 5);
        vrsteRacuna.put("Studentski", 0);
    }
}
