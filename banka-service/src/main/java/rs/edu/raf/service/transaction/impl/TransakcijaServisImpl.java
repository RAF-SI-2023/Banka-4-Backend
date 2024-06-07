package rs.edu.raf.service.transaction.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import rs.edu.raf.model.dto.transaction.RealizacijaTransakcije;
import rs.edu.raf.model.entities.racun.*;
import rs.edu.raf.repository.racun.InvoiceRepository;
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
    private final RacunRepository racunRepository;
    private final InvoiceRepository invoiceRepository;


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
                    Long racunPrimaoca = u.getRacunPrimaoca();
                    Long racunPosiljaoca = u.getRacunPosiljaoca();

                    System.out.println("Racun primaoca: " + racunPrimaoca);
//                    System.out.println(racunPosiljaoca);

                    UplataDTO uplataDTO = TransakcijaMapper.PlacanjeToDto(u);
                    //TODO ubaciti u queue i za primaoca i za posiljaoca
//                    messagingTemplate.convertAndSend("/topic/uplata/" + idUplate, uplataDTO);
//                    messagingTemplate.convertAndSend("/topic/uplata/" + racunPrimaoca, uplataDTO);
//                    messagingTemplate.convertAndSend("/topic/uplata/" + racunPosiljaoca, uplataDTO);
                    messagingTemplate.convertAndSend("/topic/uplata", uplataDTO);

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

                    Long racunPrimaoca = prenosSredstavaDTO.getPrviRacun();
                    Long racunPosiljaoca = prenosSredstavaDTO.getDrugiRacun();

//                    messagingTemplate.convertAndSend("/topic/prenos-sredstava/" + idPrenosaSredstava, prenosSredstavaDTO);
                    messagingTemplate.convertAndSend("/topic/prenos-sredstava/" + racunPrimaoca, prenosSredstavaDTO);
                    messagingTemplate.convertAndSend("/topic/prenos-sredstava/" + racunPosiljaoca, prenosSredstavaDTO);
                    return p;
                })
                .orElseThrow(() -> new EntityNotFoundException("Prenos sredstava sa ID-om " + idPrenosaSredstava + " nije pronađen."));
    }

    // TODO: dodati @Leader anotaciju za kubernetes
//    @Scheduled(cron = "0 */3 * * * *")
    @Scheduled(initialDelay = 180000, fixedRate = 180000)
    public void realizacijaTransakcija() {
//        prenosSredstavaRepository.deleteAll();
        prenosSredstavaRepository.findAll().forEach(System.out::println);
            for (PrenosSredstava p : prenosSredstavaRepository.findAllByStatus(Status.U_OBRADI)) {
                try {
                    Racun posiljalac = racunRepository.findByBrojRacuna(p.getRacunPosiljaoca()).orElse(null);
                    Racun primalac = racunRepository.findByBrojRacuna(p.getRacunPrimaoca()).orElse(null);
                    if (posiljalac == null || primalac == null) {
                        p.setStatus(Status.NEUSPELO);
                        p.setVremeIzvrsavanja(System.currentTimeMillis());
                        prenosSredstavaRepository.save(p);
                    } else {
                        if (posiljalac.getCurrency().equals(primalac.getCurrency())) {
                            boolean uspelo = sablonTransakcijeRepository.obradaTransakcije(p.getRacunPosiljaoca(), p.getRacunPrimaoca(), p.getIznos(), p.getIznos());
                            if (uspelo) p.setStatus(Status.REALIZOVANO);
                            else p.setStatus(Status.NEUSPELO);
                            p.setVremeIzvrsavanja(System.currentTimeMillis());
                            prenosSredstavaRepository.save(p);
                        } else {
                            BigDecimal iznosPrimaocu = exchangeRateServiceImpl.convert(posiljalac.getCurrency(), primalac.getCurrency(), p.getIznos());
                            boolean uspelo = sablonTransakcijeRepository.obradaTransakcije(p.getRacunPosiljaoca(), p.getRacunPrimaoca(), p.getIznos(), iznosPrimaocu);
                            if (uspelo) {
                                p.setStatus(Status.REALIZOVANO);
                                ExchangeInvoice exchangeInvoice = new ExchangeInvoice();
                                exchangeInvoice.setSenderAccount(posiljalac.getBrojRacuna().toString());
                                exchangeInvoice.setToAccount(primalac.getBrojRacuna().toString());
                                exchangeInvoice.setSenderAmount(p.getIznos());
                                exchangeInvoice.setSenderCurrency(posiljalac.getCurrency());
                                exchangeInvoice.setToCurrency(primalac.getCurrency());
                                exchangeInvoice.setExchangeRate(exchangeRateServiceImpl.exchangeRate(posiljalac.getCurrency(), primalac.getCurrency()));
                                exchangeInvoice.setProfit(p.getIznos().multiply(new BigDecimal("0.005")));
                                exchangeInvoice.setDateAndTime(System.currentTimeMillis());
                                invoiceRepository.save(exchangeInvoice);
                            } else p.setStatus(Status.NEUSPELO);
                            p.setVremeIzvrsavanja(System.currentTimeMillis());
                            prenosSredstavaRepository.save(p);
                        }
                    }
                    webSockerPrenosSredstava(p);
                } catch (Exception e) {
                    e.printStackTrace();
                    p.setStatus(Status.NEUSPELO);
                    p.setVremeIzvrsavanja(System.currentTimeMillis());
                    prenosSredstavaRepository.save(p);
                }
            }
            for (Uplata p : uplataRepository.findAllByStatus(Status.U_OBRADI)) {
                try {
                    Racun posiljalac = racunRepository.findByBrojRacuna(p.getRacunPosiljaoca()).orElse(null);
                    Racun primalac = racunRepository.findByBrojRacuna(p.getRacunPrimaoca()).orElse(null);
                    if (posiljalac == null || primalac == null) {
                        p.setStatus(Status.NEUSPELO);
                        p.setVremeIzvrsavanja(System.currentTimeMillis());
                        uplataRepository.save(p);
                    } else {
                        if (posiljalac.getCurrency().equals(primalac.getCurrency())) {
                            boolean uspelo = sablonTransakcijeRepository.obradaTransakcije(p.getRacunPosiljaoca(), p.getRacunPrimaoca(), p.getIznos(), p.getIznos());
                            if (uspelo) p.setStatus(Status.REALIZOVANO);
                            else p.setStatus(Status.NEUSPELO);
                            p.setVremeIzvrsavanja(System.currentTimeMillis());
                            uplataRepository.save(p);
                        } else {
                            BigDecimal iznosPrimaocu = exchangeRateServiceImpl.convert(posiljalac.getCurrency(), primalac.getCurrency(), p.getIznos());
                            boolean uspelo = sablonTransakcijeRepository.obradaTransakcije(p.getRacunPosiljaoca(), p.getRacunPrimaoca(), p.getIznos(), iznosPrimaocu);
                            if (uspelo) {
                                p.setStatus(Status.REALIZOVANO);
                                ExchangeInvoice exchangeInvoice = new ExchangeInvoice();
                                exchangeInvoice.setSenderAccount(posiljalac.getBrojRacuna().toString());
                                exchangeInvoice.setToAccount(primalac.getBrojRacuna().toString());
                                exchangeInvoice.setSenderAmount(p.getIznos());
                                exchangeInvoice.setSenderCurrency(posiljalac.getCurrency());
                                exchangeInvoice.setToCurrency(primalac.getCurrency());
                                exchangeInvoice.setExchangeRate(exchangeRateServiceImpl.exchangeRate(posiljalac.getCurrency(), primalac.getCurrency()));
                                exchangeInvoice.setProfit(p.getIznos().multiply(new BigDecimal("0.005")));
                                exchangeInvoice.setDateAndTime(System.currentTimeMillis());
                                invoiceRepository.save(exchangeInvoice);
                            } else p.setStatus(Status.NEUSPELO);
                            p.setVremeIzvrsavanja(System.currentTimeMillis());
                            uplataRepository.save(p);
                        }
                    }
                    webSocketUplata(p);
                } catch (Exception e) {
                    e.printStackTrace();
                    p.setStatus(Status.NEUSPELO);
                    p.setVremeIzvrsavanja(System.currentTimeMillis());
                    uplataRepository.save(p);
                }
            }
    }

    private void webSocketUplata(Uplata uplata) {
        System.out.println(uplata + " web socket");
    }

    private void webSockerPrenosSredstava(PrenosSredstava prenosSredstava) {
        System.out.println(prenosSredstava + " web socket");
    }


//    public void neuspelaUplata(String tip, Uplata uplata){
//        switch (tip){
//            case "PravniRacun" -> {
//                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
//                pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().add(uplata.getIznos()));
//                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
//            }
//            case  "DevizniRacun" -> {
//                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
//                devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().add(uplata.getIznos()));
//                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
//            }
//            case "TekuciRacun" -> {
//                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(uplata.getRacunPosiljaoca());
//                tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().add(uplata.getIznos()));
//                promeniStatusUplate(uplata.getId(), Status.NEUSPELO, System.currentTimeMillis());
//            }
//        }
//    }
//
//    public void neuspeoPrenos(String tip, PrenosSredstava prenosSredstava){
//        switch (tip){
//            case "PravniRacun" -> {
//                PravniRacun pravniRacun = racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
//                pravniRacun.setRaspolozivoStanje(pravniRacun.getRaspolozivoStanje().add(prenosSredstava.getIznos()));
//                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
//            }
//            case  "DevizniRacun" -> {
//                DevizniRacun devizniRacun = racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
//                devizniRacun.setRaspolozivoStanje(devizniRacun.getRaspolozivoStanje().add(prenosSredstava.getIznos()));
//                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
//            }
//            case "TekuciRacun" -> {
//                TekuciRacun tekuciRacun = racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(prenosSredstava.getRacunPosiljaoca());
//                tekuciRacun.setRaspolozivoStanje(tekuciRacun.getRaspolozivoStanje().add(prenosSredstava.getIznos()));
//                promeniStatusPrenosaSredstava(prenosSredstava.getId(), Status.NEUSPELO, System.currentTimeMillis());
//            }
//        }
//    }

//    public boolean proveriZajednickiElement(String[] niz1, String[] niz2) {
//        for (int i = 0; i < niz1.length; i++) {
//            for (int j = 0; j < niz2.length; j++) {
//                if (niz1[i].equals(niz2[j])) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }



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
