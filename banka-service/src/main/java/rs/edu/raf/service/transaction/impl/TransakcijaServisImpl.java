package rs.edu.raf.service.transaction.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import rs.edu.raf.model.dto.transaction.RealizacijaTransakcije;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.service.ExchangeRateServiceImpl;
import rs.edu.raf.service.racun.RacunServis;
import rs.edu.raf.model.dto.transaction.PrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.UplataDTO;
import rs.edu.raf.model.entities.transaction.PrenosSredstava;
import rs.edu.raf.model.entities.transaction.SablonTransakcije;
import rs.edu.raf.model.entities.transaction.Uplata;
import rs.edu.raf.repository.transaction.*;
import rs.edu.raf.service.transaction.TransakcijaServis;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import rs.edu.raf.model.dto.transaction.NoviPrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.NovaUplataDTO;
import rs.edu.raf.model.entities.transaction.Status;
import rs.edu.raf.model.mapper.transaction.TransakcijaMapper;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class TransakcijaServisImpl implements TransakcijaServis {

    private final UplataRepository uplataRepository;
    private final PrenosSredstavaRepository prenosSredstavaRepository;
    private final SablonTransakcijeRepository sablonTransakcijeRepository;

    private final PravniRacunRepository pravniRacunRepository;
    private final TekuciRacunRepository tekuciRacunRepository;
    private final DevizniRacunRepository devizniRacunRepository;
    private final ExchangeRateServiceImpl exchangeRateServiceImpl;

    private final RacunServis racunServis;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    /////////////////////////////////////////////////////////////////////////

    @Override
    public PrenosSredstavaDTO dobaviPrenosSretstavaDTOPoID(String id) {
        return vratiPrenosSredstavaDtoPoId(id);
    }

    @Override
    public UplataDTO dobaciUplatuSretstavaDTOPoID(String id) {
        return vratiUplatuDtoPoId(id);
    }


    @Override
    public List<PrenosSredstavaDTO> dobaviPrenosSretstavaDTOPoBrojuPrimaoca(Long brojPrimaoca) {
        return vratiPrenosSredstavaDtoPoRacunuPrimaoca(brojPrimaoca);
    }

    @Override
    public List<UplataDTO> dobaciUplatuSretstavaDTOPoBrojuPrimaoca(Long brojPrimaoca) {
        return vratiUplataDtoPoRacunuPrimaoca(brojPrimaoca);
    }

    @Override
    public List<PrenosSredstavaDTO> dobaviPrenosSretstavaDTOPoBrojuPosiljaoca(Long brojPosiljaoca) {
        return vratiPrenosSredstavaDtoPoRacunuPosiljaoca(brojPosiljaoca);
    }

    @Override
    public List<UplataDTO> dobaciUplatuSretstavaDTOPoBrojuPosiljaoca(Long brojPosiljaoca) {
        return vratiUplataDtoPoRacunuPosiljaoca(brojPosiljaoca);
    }

    /////////////////////////////////////////////////////////////////////////

    @Override
    public PrenosSredstava sacuvajPrenosSredstava(NoviPrenosSredstavaDTO noviPrenosSredstavaDTO) {

        switch(racunServis.nadjiVrstuRacuna(noviPrenosSredstavaDTO.getRacunPosiljaoca())) {
            case "PravniRacun":
                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(noviPrenosSredstavaDTO.getRacunPosiljaoca());
                if (pravniRacun == null) return null;
                if (pravniRacun.getRaspolozivoStanje().compareTo(noviPrenosSredstavaDTO.getIznos()) < 0) return null;
                pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().subtract(noviPrenosSredstavaDTO.getIznos()));
                pravniRacunRepository.save(pravniRacun);
                break;
            case "DevizniRacun":
                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(noviPrenosSredstavaDTO.getRacunPosiljaoca());
                if (devizniRacun == null) return null;
                if (devizniRacun.getRaspolozivoStanje().compareTo(noviPrenosSredstavaDTO.getIznos()) < 0) return null;
                devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().subtract(noviPrenosSredstavaDTO.getIznos()));
                devizniRacunRepository.save(devizniRacun);
                break;
            case "TekuciRacun":
                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(noviPrenosSredstavaDTO.getRacunPosiljaoca());
                if (tekuciRacun == null) return null;
                if (tekuciRacun.getRaspolozivoStanje().compareTo(noviPrenosSredstavaDTO.getIznos()) < 0) return null;
                tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().subtract(noviPrenosSredstavaDTO.getIznos()));
                tekuciRacunRepository.save(tekuciRacun);
                break;
        }

        return prenosSredstavaRepository.save(TransakcijaMapper.NoviPrenosSredstavaDtoToEntity(noviPrenosSredstavaDTO));
    }

    @Override
    public Uplata sacuvajUplatu(NovaUplataDTO novaUplataDTO) {

        switch(racunServis.nadjiVrstuRacuna(novaUplataDTO.getRacunPosiljaoca())) {
            case "PravniRacun":
                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(novaUplataDTO.getRacunPosiljaoca());
                if (pravniRacun == null) return null;
                if (pravniRacun.getRaspolozivoStanje().compareTo(novaUplataDTO.getIznos()) < 0) return null;
                pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().subtract(novaUplataDTO.getIznos()));
                pravniRacunRepository.save(pravniRacun);
                break;
            case "DevizniRacun":
                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(novaUplataDTO.getRacunPosiljaoca());
                if (devizniRacun == null) return null;
                if (devizniRacun.getRaspolozivoStanje().compareTo(novaUplataDTO.getIznos()) < 0) return null;
                devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().subtract(novaUplataDTO.getIznos()));
                devizniRacunRepository.save(devizniRacun);
                break;
            case "TekuciRacun":
                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(novaUplataDTO.getRacunPosiljaoca());
                if (tekuciRacun == null) return null;
                if (tekuciRacun.getRaspolozivoStanje().compareTo(novaUplataDTO.getIznos()) < 0) return null;
                tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().subtract(novaUplataDTO.getIznos()));
                TekuciRacun t = tekuciRacunRepository.save(tekuciRacun);
                System.out.println(t);
                break;
        }

        return uplataRepository.save(TransakcijaMapper.NovoPlacanjeDtoToEntity(novaUplataDTO));
    }

    @Override
    public Optional<PrenosSredstava> vratiPrenosSredstavaPoId(String id) {
        return prenosSredstavaRepository.findById(id);
    }

    @Override
    public Optional<Uplata> vratiUplatuPoId(String id) {
        return uplataRepository.findById(id);
    }

    @Override
    public PrenosSredstavaDTO vratiPrenosSredstavaDtoPoId(String id) {
        return prenosSredstavaRepository.findById(id)
                .map(TransakcijaMapper::PrenosSredstavaToDto)
                .orElseThrow(() -> new EntityNotFoundException("Prenos sredstava sa ID-om " + id + " nije pronađen."));
    }

    @Override
    public UplataDTO vratiUplatuDtoPoId(String id) {
        return uplataRepository.findById(id)
                .map(TransakcijaMapper::PlacanjeToDto)
                .orElseThrow(() -> new EntityNotFoundException("Placanje sa ID-om " + id + " nije pronađeno."));
    }

    @Override
    public List<PrenosSredstavaDTO> vratiPrenosSredstavaDtoPoRacunuPrimaoca(Long racunPrimaoca) {
        return prenosSredstavaRepository.findAllByRacunPrimaoca(racunPrimaoca).stream()
                .map(TransakcijaMapper::PrenosSredstavaToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UplataDTO> vratiUplataDtoPoRacunuPrimaoca(Long racunPrimaoca) {
        uplataRepository.findAllByRacunPrimaoca(racunPrimaoca).forEach(System.out::println);
        return uplataRepository.findAllByRacunPrimaoca(racunPrimaoca).stream()
                .map(TransakcijaMapper::PlacanjeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrenosSredstavaDTO> vratiPrenosSredstavaDtoPoRacunuPosiljaoca(Long racunPosiljaoca) {
        return prenosSredstavaRepository.findAllByRacunPosiljaoca(racunPosiljaoca).stream()
                .map(TransakcijaMapper::PrenosSredstavaToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UplataDTO> vratiUplataDtoPoRacunuPosiljaoca(Long racunPosiljaoca) {
        uplataRepository.findAllByRacunPosiljaoca(racunPosiljaoca).forEach(System.out::println);
        return uplataRepository.findAllByRacunPosiljaoca(racunPosiljaoca).stream()
                .map(TransakcijaMapper::PlacanjeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrenosSredstava> vratiPrenosSredstavaUObradi() {
        return prenosSredstavaRepository.findAllByStatus(Status.U_OBRADI);
    }

    @Override
    public List<Uplata> vratiUplateUObradi() {
        return uplataRepository.findAllByStatus(Status.U_OBRADI);
    }

    @Override
    public BigDecimal izracunajRezervisanaSredstva(Long brojRacuna) {

        return switch (racunServis.nadjiVrstuRacuna(brojRacuna)) {
            case "PravniRacun" -> {
                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(brojRacuna);
                yield (pravniRacun != null) ? pravniRacun.getStanje().subtract(pravniRacun.getRaspolozivoStanje()) : null;
            }
            case "DevizniRacun" -> {
                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(brojRacuna);
                yield (devizniRacun != null) ? devizniRacun.getStanje().subtract(devizniRacun.getRaspolozivoStanje()) : null;
            }
            case "TekuciRacun" -> {
                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(brojRacuna);
                yield (tekuciRacun != null) ? tekuciRacun.getStanje().subtract(tekuciRacun.getRaspolozivoStanje()) : null;
            }
            default -> null;
        };
    }

    @Override
    public BigDecimal vratiSredstva(Long idRacuna) {

        return switch (racunServis.nadjiVrstuRacuna(idRacuna)) {
            case "PravniRacun" -> {
                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoID(idRacuna);
                yield (pravniRacun != null) ? pravniRacun.getStanje() : null;
            }
            case "DevizniRacun" -> {
                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoID(idRacuna);
                yield (devizniRacun != null) ? devizniRacun.getStanje() : null;
            }
            case "TekuciRacun" -> {
                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoID(idRacuna);
                yield (tekuciRacun != null) ? tekuciRacun.getStanje() : null;
            }
            default -> null;
        };
    }

    @Override
    public Uplata promeniStatusUplate(String idUplate, String status, Long vremeIzvrsavanja) {
        return uplataRepository.findById(idUplate)
                .map(uplata -> {
                    uplata.setStatus(status);
                    uplata.setVremeIzvrsavanja(vremeIzvrsavanja);

                    Uplata u = uplataRepository.save(uplata);
                    UplataDTO uplataDTO = TransakcijaMapper.PlacanjeToDto(u);
                    //TODO ubaciti u queue i za primaoca i za posiljaoca
                    messagingTemplate.convertAndSend("/topic/uplata/" + idUplate, uplataDTO);
                    return u;
                })
                .orElseThrow(() -> new EntityNotFoundException("Uplata sa ID-om " + idUplate + " nije pronađen."));
    }

    @Override
    public PrenosSredstava promeniStatusPrenosaSredstava(String idPrenosaSredstava, String status, Long vremeIzvrsavanja) {
        return prenosSredstavaRepository.findById(idPrenosaSredstava)
                .map(prenosSredstava -> {
                    prenosSredstava.setStatus(status);
                    prenosSredstava.setVremeIzvrsavanja(vremeIzvrsavanja);
                    PrenosSredstava p = prenosSredstavaRepository.save(prenosSredstava);
                    PrenosSredstavaDTO prenosSredstavaDTO = TransakcijaMapper.PrenosSredstavaToDto(p);
                    //TODO treba ubaciti u queue i za posiljaoca i za primaoca

                    messagingTemplate.convertAndSend("/topic/prenos-sredstava/" + idPrenosaSredstava, prenosSredstavaDTO);
                    return p;
                })
                .orElseThrow(() -> new EntityNotFoundException("Prenos sredstava sa ID-om " + idPrenosaSredstava + " nije pronađen."));
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void realizacijaTransakcijaZaPrenostSredstava() {
        List<PrenosSredstava> prenosi = vratiPrenosSredstavaUObradi();

        for (PrenosSredstava prenosSredstava : prenosi) {

            RealizacijaTransakcije realizacijaTransakcijePosiljaoca = null;
            RealizacijaTransakcije realizacijaTransakcijePrimaoca = null;
            BigDecimal transferAmount;

            switch (racunServis.nadjiVrstuRacuna(prenosSredstava.getRacunPosiljaoca())) {
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                    realizacijaTransakcijePosiljaoca = new RealizacijaTransakcije(pravniRacun.getId(), pravniRacun.getAktivan(), izracunajRezervisanaSredstva(pravniRacun.getBrojRacuna()), pravniRacun.getCurrency(), "PravniRacun", pravniRacun.getStanje(), pravniRacun.getFirma());
                }
                case "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                    realizacijaTransakcijePosiljaoca = new RealizacijaTransakcije(devizniRacun.getId(), devizniRacun.getAktivan(), izracunajRezervisanaSredstva(devizniRacun.getBrojRacuna()), devizniRacun.getDefaultCurrency(), "DevizniRacun", devizniRacun.getStanje(), devizniRacun.getVlasnik());
                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                    realizacijaTransakcijePosiljaoca = new RealizacijaTransakcije(tekuciRacun.getId(), tekuciRacun.getAktivan(), izracunajRezervisanaSredstva(tekuciRacun.getBrojRacuna()), tekuciRacun.getCurrency(), "TekuciRacun", tekuciRacun.getStanje(), tekuciRacun.getVlasnik());
                }
            }

            switch (racunServis.nadjiVrstuRacuna(prenosSredstava.getRacunPrimaoca())) {
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                    realizacijaTransakcijePrimaoca = new RealizacijaTransakcije(pravniRacun.getId(), pravniRacun.getAktivan(), izracunajRezervisanaSredstva(pravniRacun.getBrojRacuna()), pravniRacun.getCurrency(), "PravniRacun", pravniRacun.getStanje(), pravniRacun.getFirma());
                }
                case "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                    realizacijaTransakcijePrimaoca = new RealizacijaTransakcije(devizniRacun.getId(), devizniRacun.getAktivan(), izracunajRezervisanaSredstva(devizniRacun.getBrojRacuna()), devizniRacun.getDefaultCurrency(), "DevizniRacun", devizniRacun.getStanje(), devizniRacun.getVlasnik());
                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                    realizacijaTransakcijePrimaoca = new RealizacijaTransakcije(tekuciRacun.getId(), tekuciRacun.getAktivan(), izracunajRezervisanaSredstva(tekuciRacun.getBrojRacuna()), tekuciRacun.getCurrency(), "TekuciRacun", tekuciRacun.getStanje(), tekuciRacun.getVlasnik());
                }
            }

            if (realizacijaTransakcijePosiljaoca == null || realizacijaTransakcijePrimaoca == null){
                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
                continue;
            }

            if(realizacijaTransakcijePosiljaoca.getIdKorisnika() != realizacijaTransakcijePrimaoca.getIdKorisnika()){
                neuspeoPrenos(realizacijaTransakcijePosiljaoca.getTipRacuna(), prenosSredstava);
                continue;
            }


            if(!realizacijaTransakcijePosiljaoca.getAktivan() || !realizacijaTransakcijePrimaoca.getAktivan()){
                neuspeoPrenos(realizacijaTransakcijePosiljaoca.getTipRacuna(), prenosSredstava);
                continue;
            }
            transferAmount = exchangeRateServiceImpl.convert(realizacijaTransakcijePosiljaoca.getValute(), realizacijaTransakcijePrimaoca.getValute(), prenosSredstava.getIznos());

            if (realizacijaTransakcijePosiljaoca.getRezervisanaSredstva().compareTo(prenosSredstava.getIznos()) < 0) {
                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
                continue;
            }

            boolean prosaoPrvi = false;
            boolean prosaoDrugi = false;

            switch (realizacijaTransakcijePosiljaoca.getTipRacuna()){
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                    pravniRacun.setStanje(pravniRacun.getStanje().subtract(prenosSredstava.getIznos()));
                    PravniRacun racun = pravniRacunRepository.save(pravniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().subtract(prenosSredstava.getIznos())) == 0){
                        prosaoPrvi = true;
                    }
                }
                case  "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                    devizniRacun.setStanje(devizniRacun.getStanje().subtract(prenosSredstava.getIznos()));
                    DevizniRacun racun = devizniRacunRepository.save(devizniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().subtract(prenosSredstava.getIznos())) == 0){
                        prosaoPrvi = true;
                    }                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                    tekuciRacun.setStanje(tekuciRacun.getStanje().subtract(prenosSredstava.getIznos()));
                    TekuciRacun racun = tekuciRacunRepository.save(tekuciRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().subtract(prenosSredstava.getIznos())) == 0){
                        prosaoPrvi = true;
                    }
                }
            }

            switch (realizacijaTransakcijePrimaoca.getTipRacuna()){
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                    pravniRacun.setStanje(pravniRacun.getStanje().add(transferAmount));
                    pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().add(transferAmount));
                    PravniRacun racun = pravniRacunRepository.save(pravniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().add(transferAmount)) == 0){
                        prosaoDrugi = true;
                    }
                }
                case  "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                    devizniRacun.setStanje(devizniRacun.getStanje().add(transferAmount));
                    devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().add(transferAmount));
                    DevizniRacun racun = devizniRacunRepository.save(devizniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().add(transferAmount)) == 0){
                        prosaoDrugi = true;
                    }

                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                    tekuciRacun.setStanje(tekuciRacun.getStanje().add(transferAmount));
                    tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().add(transferAmount));
                    TekuciRacun racun = tekuciRacunRepository.save(tekuciRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().add(transferAmount)) == 0){
                        prosaoDrugi = true;
                    }
                }
            }

            if(prosaoPrvi && prosaoDrugi){
                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.REALIZOVANO, System.currentTimeMillis());
            } else {

                switch (realizacijaTransakcijePosiljaoca.getTipRacuna()){
                    case "PravniRacun" -> {
                        PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                        pravniRacun.setStanje(realizacijaTransakcijePosiljaoca.getPrethodnoStanje());
                        pravniRacunRepository.save(pravniRacun);
                    }
                    case  "DevizniRacun" -> {
                        DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                        devizniRacun.setStanje(realizacijaTransakcijePosiljaoca.getPrethodnoStanje());
                        devizniRacunRepository.save(devizniRacun);
                    }
                    case "TekuciRacun" -> {
                        TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                        tekuciRacun.setStanje(realizacijaTransakcijePosiljaoca.getPrethodnoStanje());
                        tekuciRacunRepository.save(tekuciRacun);
                    }
                }

                neuspeoPrenos(realizacijaTransakcijePosiljaoca.getTipRacuna(), prenosSredstava);


                switch (realizacijaTransakcijePrimaoca.getTipRacuna()){
                    case "PravniRacun" -> {
                        PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                        pravniRacun.setStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje());
                        pravniRacun.setRaspolozivoStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje().subtract(realizacijaTransakcijePrimaoca.getRezervisanaSredstva()));
                        pravniRacunRepository.save(pravniRacun);

                    }
                    case  "DevizniRacun" -> {
                        DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                        devizniRacun.setStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje());
                        devizniRacun.setRaspolozivoStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje().subtract(realizacijaTransakcijePrimaoca.getRezervisanaSredstva()));
                        devizniRacunRepository.save(devizniRacun);

                    }
                    case "TekuciRacun" -> {
                        TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPrimaoca());
                        tekuciRacun.setStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje());
                        tekuciRacun.setRaspolozivoStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje().subtract(realizacijaTransakcijePrimaoca.getRezervisanaSredstva()));
                        tekuciRacunRepository.save(tekuciRacun);
                    }
                }
                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.REALIZOVANO, System.currentTimeMillis());
            }
        }


    }

    @Scheduled(cron = "0 */2 * * * *")
    public void realizacijaTransakcijaZaUplatu() {
        List<Uplata> uplate = vratiUplateUObradi();

        for (Uplata uplata : uplate) {
            RealizacijaTransakcije realizacijaTransakcijePosiljaoca = null;
            RealizacijaTransakcije realizacijaTransakcijePrimaoca = null;
            BigDecimal transferAmoun;

            switch (racunServis.nadjiVrstuRacuna(uplata.getRacunPosiljaoca())) {
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
                    realizacijaTransakcijePosiljaoca = new RealizacijaTransakcije(pravniRacun.getBrojRacuna(), pravniRacun.getAktivan(), izracunajRezervisanaSredstva(pravniRacun.getBrojRacuna()), pravniRacun.getCurrency(), "PravniRacun", pravniRacun.getStanje(), pravniRacun.getFirma());
                }
                case "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
                    realizacijaTransakcijePosiljaoca = new RealizacijaTransakcije();
                    realizacijaTransakcijePosiljaoca.setBrojRacuna(devizniRacun.getBrojRacuna());
                    realizacijaTransakcijePosiljaoca.setAktivan(devizniRacun.getAktivan());
                    realizacijaTransakcijePosiljaoca.setRezervisanaSredstva(izracunajRezervisanaSredstva(devizniRacun.getBrojRacuna()));
                    realizacijaTransakcijePosiljaoca.setValute(devizniRacun.getDefaultCurrency());
                    realizacijaTransakcijePosiljaoca.setTipRacuna("DevizniRacun");
                    realizacijaTransakcijePosiljaoca.setPrethodnoStanje(devizniRacun.getStanje());
                    realizacijaTransakcijePosiljaoca.setIdKorisnika(devizniRacun.getVlasnik());
                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
                    realizacijaTransakcijePosiljaoca = new RealizacijaTransakcije();
                    realizacijaTransakcijePosiljaoca.setBrojRacuna(tekuciRacun.getBrojRacuna());
                    realizacijaTransakcijePosiljaoca.setAktivan(tekuciRacun.getAktivan());
                    realizacijaTransakcijePosiljaoca.setRezervisanaSredstva(izracunajRezervisanaSredstva(tekuciRacun.getBrojRacuna()));
                    realizacijaTransakcijePosiljaoca.setValute(tekuciRacun.getCurrency());
                    realizacijaTransakcijePosiljaoca.setTipRacuna("TekuciRacun");
                    realizacijaTransakcijePosiljaoca.setPrethodnoStanje(tekuciRacun.getStanje());
                    realizacijaTransakcijePosiljaoca.setIdKorisnika(tekuciRacun.getVlasnik());
                }
            }

            switch (racunServis.nadjiVrstuRacuna(uplata.getRacunPrimaoca())) {
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPrimaoca());
                    realizacijaTransakcijePrimaoca = new RealizacijaTransakcije(pravniRacun.getBrojRacuna(), pravniRacun.getAktivan(), izracunajRezervisanaSredstva(pravniRacun.getBrojRacuna()), pravniRacun.getCurrency(), "PravniRacun", pravniRacun.getStanje(), pravniRacun.getFirma());
                }
                case "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(uplata.getRacunPrimaoca());
                    realizacijaTransakcijePrimaoca = new RealizacijaTransakcije(devizniRacun.getBrojRacuna(), devizniRacun.getAktivan(), izracunajRezervisanaSredstva(devizniRacun.getBrojRacuna()), devizniRacun.getDefaultCurrency(), "DevizniRacun", devizniRacun.getStanje(), devizniRacun.getVlasnik());
                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(uplata.getRacunPrimaoca());
                    realizacijaTransakcijePrimaoca = new RealizacijaTransakcije();
                    realizacijaTransakcijePrimaoca.setBrojRacuna(tekuciRacun.getBrojRacuna());
                    realizacijaTransakcijePrimaoca.setAktivan(tekuciRacun.getAktivan());
                    realizacijaTransakcijePrimaoca.setRezervisanaSredstva(izracunajRezervisanaSredstva(tekuciRacun.getBrojRacuna()));
                    realizacijaTransakcijePrimaoca.setValute(tekuciRacun.getCurrency());
                    realizacijaTransakcijePrimaoca.setTipRacuna("TekuciRacun");
                    realizacijaTransakcijePrimaoca.setPrethodnoStanje(tekuciRacun.getStanje());
                    realizacijaTransakcijePrimaoca.setIdKorisnika(tekuciRacun.getVlasnik());                }
            }

            if (realizacijaTransakcijePosiljaoca == null || realizacijaTransakcijePrimaoca == null){
                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
                continue;
            }

            if(!realizacijaTransakcijePosiljaoca.getAktivan() || !realizacijaTransakcijePrimaoca.getAktivan()){
                neuspelaUplata(realizacijaTransakcijePosiljaoca.getTipRacuna(), uplata);
                continue;
            }

            transferAmoun = exchangeRateServiceImpl.convert(realizacijaTransakcijePosiljaoca.getValute(), realizacijaTransakcijePrimaoca.getValute(), uplata.getIznos());

            if (realizacijaTransakcijePosiljaoca.getRezervisanaSredstva().compareTo(uplata.getIznos()) < 0) {
                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
                continue;
            }

            boolean prosaoPrvi = false;
            boolean prosaoDrugi = false;

            switch (realizacijaTransakcijePosiljaoca.getTipRacuna()){
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(realizacijaTransakcijePosiljaoca.getBrojRacuna());
                    pravniRacun.setStanje(pravniRacun.getStanje().subtract(uplata.getIznos()));
                    PravniRacun racun = pravniRacunRepository.save(pravniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().subtract(uplata.getIznos())) == 0){
                        prosaoPrvi = true;
                    }
                }
                case  "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(realizacijaTransakcijePosiljaoca.getBrojRacuna());
                    devizniRacun.setStanje(devizniRacun.getStanje().subtract(uplata.getIznos()));
                    DevizniRacun racun = devizniRacunRepository.save(devizniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().subtract(uplata.getIznos())) == 0){
                        prosaoPrvi = true;
                    }                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(realizacijaTransakcijePosiljaoca.getBrojRacuna());
                    tekuciRacun.setStanje(tekuciRacun.getStanje().subtract(uplata.getIznos()));
                    TekuciRacun racun = tekuciRacunRepository.save(tekuciRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().subtract(uplata.getIznos())) == 0){
                        prosaoPrvi = true;
                    }
                }
            }

            switch (realizacijaTransakcijePrimaoca.getTipRacuna()){
                case "PravniRacun" -> {
                    PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(realizacijaTransakcijePrimaoca.getBrojRacuna());
                    pravniRacun.setStanje(pravniRacun.getStanje().add(transferAmoun));
                    pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().add(transferAmoun));
                    PravniRacun racun = pravniRacunRepository.save(pravniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().add(transferAmoun)) == 0){
                        prosaoDrugi = true;
                    }
                }
                case  "DevizniRacun" -> {
                    DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(realizacijaTransakcijePrimaoca.getBrojRacuna());
                    devizniRacun.setStanje(devizniRacun.getStanje().add(transferAmoun));
                    devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().add(transferAmoun));
                    DevizniRacun racun = devizniRacunRepository.save(devizniRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePosiljaoca.getPrethodnoStanje().add(transferAmoun)) == 0){
                        prosaoDrugi = true;
                    }

                }
                case "TekuciRacun" -> {
                    TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(realizacijaTransakcijePrimaoca.getBrojRacuna());
                    tekuciRacun.setStanje(tekuciRacun.getStanje().add(transferAmoun));
                    tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().add(transferAmoun));
                    TekuciRacun racun = tekuciRacunRepository.save(tekuciRacun);
                    if(racun.getStanje().compareTo(realizacijaTransakcijePrimaoca.getPrethodnoStanje().add(transferAmoun)) == 0){
                        prosaoDrugi = true;
                    }
                }
            }

            if(prosaoPrvi && prosaoDrugi){
                promeniStatusUplate(uplata.getId(), Status.REALIZOVANO, System.currentTimeMillis());
            } else {

                switch (realizacijaTransakcijePosiljaoca.getTipRacuna()){
                    case "PravniRacun" -> {
                        PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(realizacijaTransakcijePosiljaoca.getBrojRacuna());
                        pravniRacun.setStanje(realizacijaTransakcijePosiljaoca.getPrethodnoStanje());
                        pravniRacunRepository.save(pravniRacun);
                    }
                    case  "DevizniRacun" -> {
                        DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(realizacijaTransakcijePosiljaoca.getBrojRacuna());
                        devizniRacun.setStanje(realizacijaTransakcijePosiljaoca.getPrethodnoStanje());
                        devizniRacunRepository.save(devizniRacun);
                    }
                    case "TekuciRacun" -> {
                        TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(realizacijaTransakcijePosiljaoca.getBrojRacuna());
                        tekuciRacun.setStanje(realizacijaTransakcijePosiljaoca.getPrethodnoStanje());
                        tekuciRacunRepository.save(tekuciRacun);
                    }
                }

                neuspelaUplata(realizacijaTransakcijePosiljaoca.getTipRacuna(), uplata);


                switch (realizacijaTransakcijePrimaoca.getTipRacuna()){
                    case "PravniRacun" -> {
                        PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(realizacijaTransakcijePrimaoca.getBrojRacuna());
                        pravniRacun.setStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje());
                        pravniRacun.setRaspolozivoStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje().subtract(realizacijaTransakcijePrimaoca.getRezervisanaSredstva()));
                        pravniRacunRepository.save(pravniRacun);
                    }
                    case  "DevizniRacun" -> {
                        DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(realizacijaTransakcijePrimaoca.getBrojRacuna());
                        devizniRacun.setStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje());
                        devizniRacun.setRaspolozivoStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje().subtract(realizacijaTransakcijePrimaoca.getRezervisanaSredstva()));
                        devizniRacunRepository.save(devizniRacun);
                    }
                    case "TekuciRacun" -> {
                        TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(realizacijaTransakcijePrimaoca.getBrojRacuna());
                        tekuciRacun.setStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje());
                        tekuciRacun.setRaspolozivoStanje(realizacijaTransakcijePrimaoca.getPrethodnoStanje().subtract(realizacijaTransakcijePrimaoca.getRezervisanaSredstva()));
                        tekuciRacunRepository.save(tekuciRacun);
                    }
                }
                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
            }
        }
    }

    public void neuspelaUplata(String tip, Uplata uplata){
        switch (tip){
            case "PravniRacun" -> {
                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
                pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().add(uplata.getIznos()));
                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
            }
            case  "DevizniRacun" -> {
                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
                devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().add(uplata.getIznos()));
                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
            }
            case "TekuciRacun" -> {
                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
                tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().add(uplata.getIznos()));
                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
            }
        }
    }

    public void neuspeoPrenos(String tip, PrenosSredstava prenosSredstava){
        switch (tip){
            case "PravniRacun" -> {
                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().add(prenosSredstava.getIznos()));
                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
            }
            case  "DevizniRacun" -> {
                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().add(prenosSredstava.getIznos()));
                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
            }
            case "TekuciRacun" -> {
                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
                tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().add(prenosSredstava.getIznos()));
                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
            }
        }
    }

    public boolean proveriZajednickiElement(String[] niz1, String[] niz2) {
        for (int i = 0; i < niz1.length; i++) {
            for (int j = 0; j < niz2.length; j++) {
                if (niz1[i].equals(niz2[j])) {
                    return true;
                }
            }
        }
        return false;
    }



    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////



    @Override
    public boolean proveraIspravnostiUplataTransakcije(Uplata uplata) {

        return false;
    }

    @Override
    public boolean proveraIspravnostiPrenosSredstavaTransakcije(PrenosSredstava prenosSredstava) {
        return false;
    }

    @Override
    public List<SablonTransakcije> getSavedTransactionalPatterns() {
        return this.sablonTransakcijeRepository.findAll();
    }

    @Override
    public SablonTransakcije addNewTransactionalPattern(SablonTransakcije sablonTransakcije) {
        if(sablonTransakcije != null) {           //AKO VEC POSTOJI AZURIRACE,AKO NE POSTOJI DODACE
            return this.sablonTransakcijeRepository.save(sablonTransakcije);
        }
        return null;
    }

    @Override
    public boolean deleteTransactionalPattern(Long transactionPatternId) {

        if(transactionPatternId != null){
            //NE VRACA GRESKU AKO NE POSTOJI ID
            this.sablonTransakcijeRepository.deleteById(transactionPatternId);
            return true;
        }
        return false;
    }

    @Override
    public void deleteAllTransactionalPatterns() {
        //NE VRACA GRESKU AKO NEMA STA DA SE BRISE
        this.sablonTransakcijeRepository.deleteAll();
    }

}
