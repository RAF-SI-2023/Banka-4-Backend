package rs.edu.raf.service.racun.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import rs.edu.raf.exceptions.BankAccountNotFoundException;
import rs.edu.raf.exceptions.CompanyNotFoundException;
import rs.edu.raf.model.dto.KorisnikDTO;
import rs.edu.raf.model.dto.racun.*;
import rs.edu.raf.model.entities.racun.*;
import rs.edu.raf.model.mapper.racun.FirmaMapper;
import rs.edu.raf.model.mapper.racun.RacunMapper;
import rs.edu.raf.repository.racun.FirmaRepository;
import rs.edu.raf.repository.transaction.DevizniRacunRepository;
import rs.edu.raf.repository.transaction.PravniRacunRepository;
import rs.edu.raf.repository.transaction.RacunRepository;
import rs.edu.raf.repository.transaction.TekuciRacunRepository;
import okhttp3.*;
import rs.edu.raf.service.racun.RacunServis;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class RacunServisImpl implements RacunServis {

    private DevizniRacunRepository devizniRacunRepository;
    private PravniRacunRepository pravniRacunRepository;
    private TekuciRacunRepository tekuciRacunRepository;
    private FirmaRepository firmaRepository;

    private RacunMapper racunMapper;
    private FirmaMapper firmaMapper;
    private RestTemplate restTemplate;
    private RacunRepository racunRepository;

    @Autowired
    public RacunServisImpl(DevizniRacunRepository devizniRacunRepository, PravniRacunRepository pravniRacunRepository, TekuciRacunRepository tekuciRacunRepository, RestTemplate restTemplate, FirmaRepository firmaRepository, RacunMapper racunMapper, FirmaMapper firmaMapper, RacunRepository racunRepository) {
        this.devizniRacunRepository = devizniRacunRepository;
        this.pravniRacunRepository = pravniRacunRepository;
        this.tekuciRacunRepository = tekuciRacunRepository;
        this.firmaRepository = firmaRepository;
        this.racunMapper = racunMapper;
        this.firmaMapper = firmaMapper;
        this.restTemplate = restTemplate;
        this.racunRepository = racunRepository;
    }

    @Override
    public DevizniRacun kreirajDevizniRacun(NoviDevizniRacunDTO noviDevizniRacunDTO) {
        DevizniRacun dr = racunMapper.noviDevizniRacunDTOToDevizniRacun(noviDevizniRacunDTO);
        DevizniRacun drRepo = this.devizniRacunRepository.save(dr);

        Request request = new Request.Builder()
                .url("http://user-service:8080/api/korisnik/addAccount/" + drRepo.getVlasnik() + "/" + drRepo.getBrojRacuna())
                .get()
                .build();

        Call call = new OkHttpClient().newCall(request);

        try {
            Response response = call.execute();
            if(response.code() == 200){
                return drRepo;
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public PravniRacun kreirajPravniRacun(NoviPravniRacunDTO noviPravniRacunDTO) {
        PravniRacun pr = racunMapper.noviPravniRacunDTOToPravniRacun(noviPravniRacunDTO);
        PravniRacun prRepo = this.pravniRacunRepository.save(pr);
        Firma firma = firmaRepository.findById(prRepo.getVlasnik()).orElseThrow(()->new CompanyNotFoundException("Company with id " + prRepo.getVlasnik() + " doesn't exist!"));
        dodajPravniRacunFirmi(prRepo,firma);
        return prRepo;
    }

    @Override
    public TekuciRacun kreirajTekuciRacun(NoviTekuciRacunDTO noviTekuciRacunDTO) {
        TekuciRacun tr = racunMapper.noviTekuciRacunDTOToTekuciRacun(noviTekuciRacunDTO);
        TekuciRacun trRepo = this.tekuciRacunRepository.save(tr);
        Request request = new Request.Builder()
                .url("http://user-service:8080/api/korisnik/addAccount/" + trRepo.getVlasnik() + "/" + trRepo.getBrojRacuna())
                .get()
                .build();

        Call call = new OkHttpClient().newCall(request);

        try {
            Response response = call.execute();
            if(response.code() == 200){
                return trRepo;
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public List<RacunDTO> izlistavanjeRacunaJednogKorisnika(Long idKorisnika, String token) {



//        ResponseEntity<KorisnikDTO> response = restTemplate.exchange("/korisnik/id/" + idKorisnika, HttpMethod.GET, null, KorisnikDTO.class);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        // Kreirajte HttpEntity sa HttpHeaders
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<KorisnikDTO> response = restTemplate.exchange("/korisnik/id/" + idKorisnika, HttpMethod.GET,entity,KorisnikDTO.class);
        KorisnikDTO korisnikDTO = response.getBody();

        List<RacunDTO> racunDTOs = new ArrayList<>();
        if (korisnikDTO != null && korisnikDTO.getPovezaniRacuni()!= null) {
            RacunDTO dto;
            List<String> racuni = List.of(korisnikDTO.getPovezaniRacuni().split(","));
            for (String r : racuni) {
                String vrsta = nadjiVrstuRacuna(Long.parseLong(r));
                if (Objects.equals(vrsta, "DevizniRacun")) {
                    DevizniRacun dr = this.devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(Long.parseLong(r)).orElse(null);
                    if (dr != null) {
                        dto = racunMapper.devizniRacunToRacunDTO(dr);
                        racunDTOs.add(dto);
                    }
                } else if (Objects.equals(vrsta, "TekuciRacun")) {
                    TekuciRacun tr = this.tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(Long.parseLong(r)).orElse(null);
                    if (tr != null) {
                        dto = racunMapper.tekuciRacunToRacunDTO(tr);
                        racunDTOs.add(dto);
                    }
                }
            }
        } else {
            Firma f = firmaRepository.findById(idKorisnika).orElseThrow(()->new CompanyNotFoundException("Company from user with id " + idKorisnika + " doesn't exist!"));
            if (f != null) {
                RacunDTO dto;
                List<String> racuni = List.of(f.getPovezaniRacuni().split(","));
                for (String r : racuni) {
                    PravniRacun pr = this.pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(Long.parseLong(r)).orElse(null);
                    if (pr != null) {
                        dto = racunMapper.pravniRacunToRacunDTO(pr);
                        racunDTOs.add(dto);
                    }
                }
            }
        }
        return racunDTOs;
    }

    @Override
    public RacunDTO nadjiAktivanRacunPoID(Long id) {
        DevizniRacun dr = this.devizniRacunRepository.findByIdAndAktivanIsTrue(id).orElse(null);
        PravniRacun pr = this.pravniRacunRepository.findByIdAndAktivanIsTrue(id).orElse(null);
        TekuciRacun tr = this.tekuciRacunRepository.findByIdAndAktivanIsTrue(id).orElse(null);
        RacunDTO dto;
        if (dr != null) {
            dto = racunMapper.devizniRacunToRacunDTO(dr);
            return dto;
        } else if (pr != null) {
            dto = racunMapper.pravniRacunToRacunDTO(pr);
            return dto;
        } else if (tr != null) {
            dto = racunMapper.tekuciRacunToRacunDTO(tr);
            return dto;
        }
        throw new BankAccountNotFoundException("Bank account " + id + " not found!");
    }

    @Override
    public DevizniRacun nadjiAktivanDevizniRacunPoID(Long id) {
        return this.devizniRacunRepository.findByIdAndAktivanIsTrue(id).orElseThrow(()->new BankAccountNotFoundException("Bank account " + id + " not found!"));
    }

    @Override
    public PravniRacun nadjiAktivanPravniRacunPoID(Long id) {
        return this.pravniRacunRepository.findByIdAndAktivanIsTrue(id).orElseThrow(()->new BankAccountNotFoundException("Bank account " + id + " not found!"));
    }

    @Override
    public TekuciRacun nadjiAktivanTekuciRacunPoID(Long id) {
        return this.tekuciRacunRepository.findByIdAndAktivanIsTrue(id).orElseThrow(()->new BankAccountNotFoundException("Bank account " + id + " not found!"));
    }

    @Override
    public RacunDTO nadjiAktivanRacunPoBrojuRacuna(Long BrojRacuna) {
        RacunDTO dto;
        if (BrojRacuna % 100 == 11) {
            DevizniRacun dr = this.devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(BrojRacuna).orElseThrow(()->new BankAccountNotFoundException("Bank account " + BrojRacuna + " not found!"));
            if (dr != null) {
                dto = racunMapper.devizniRacunToRacunDTO(dr);
                return dto;
            }
        } else if (BrojRacuna % 100 == 22) {
            PravniRacun pr = this.pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(BrojRacuna).orElseThrow(()->new BankAccountNotFoundException("Bank account " + BrojRacuna + " not found!"));
            if (pr != null) {
                dto = racunMapper.pravniRacunToRacunDTO(pr);
                return dto;
            }
        } else if (BrojRacuna % 100 == 33) {
            TekuciRacun tr = this.tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(BrojRacuna).orElseThrow(()->new BankAccountNotFoundException("Bank account " + BrojRacuna + " not found!"));
            if (tr != null) {
                dto = racunMapper.tekuciRacunToRacunDTO(tr);
                return dto;
            }
        }
        return null;
    }

    @Override
    public DevizniRacun nadjiAktivanDevizniRacunPoBrojuRacuna(Long BrojRacuna) {
        return this.devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(BrojRacuna).orElseThrow(()->new BankAccountNotFoundException("Bank account " + BrojRacuna + " not found!"));
    }

    @Override
    public PravniRacun nadjiAktivanPravniRacunPoBrojuRacuna(Long BrojRacuna) {
        return this.pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(BrojRacuna).orElseThrow(()->new BankAccountNotFoundException("Bank account " + BrojRacuna + " not found!"));
    }

    @Override
    public TekuciRacun nadjiAktivanTekuciRacunPoBrojuRacuna(Long BrojRacuna) {
        return this.tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(BrojRacuna).orElseThrow(()->new BankAccountNotFoundException("Bank account " + BrojRacuna + " not found!"));
    }


    @Override
    public boolean dodajPravniRacunFirmi(PravniRacun pravniRacun, Firma firma) {
        pravniRacun.setVlasnik(firma.getId());
        if (firma.getPovezaniRacuni() == null) {
            firma.setPovezaniRacuni(pravniRacun.getBrojRacuna().toString());
        } else {
            List<String> racuni = List.of(firma.getPovezaniRacuni().split(","));
            for (String r : racuni) {
                if (r.equals(pravniRacun.getBrojRacuna().toString())) {
                    return false;
                }
            }
            firma.setPovezaniRacuni(firma.getPovezaniRacuni().concat("," + pravniRacun.getBrojRacuna()));
        }
        this.pravniRacunRepository.save(pravniRacun);
        this.firmaRepository.save(firma);
        return true;
    }


    @Override
    public Long generisiBrojRacuna(String tipRacuna) {
        if (tipRacuna.equals("DevizniRacun")) {
            Long id = devizniRacunRepository.findTop1ByOrderByIdDesc() * 100L;
            return 444000000000000011L + id; //444 sifra banke 0000 filial
        } else if (tipRacuna.equals("PravniRacun")) {
            Long id = pravniRacunRepository.findTop1ByOrderByIdDesc() * 100L;
            return 444000000000000022L + id; //444 sifra banke 0000 filial
        } else if (tipRacuna.equals("TekuciRacun")) {
            Long id = tekuciRacunRepository.findTop1ByOrderByIdDesc() * 100L;
            return 444000000000000033L + id; //444 sifra banke 0000 filial
        }
        return null;
    }

    @Override
    public String nadjiVrstuRacuna(Long BrojRacuna) {
        if (BrojRacuna % 100 == 11) {
            return "DevizniRacun";
        } else if (BrojRacuna % 100 == 22) {
            return "PravniRacun";
        } else if (BrojRacuna % 100 == 33) {
            return "TekuciRacun";
        }
        return null;
    }

    @Override
    public List<FirmaDTO> izlistajSveFirme() {
        List<FirmaDTO> dtos = new ArrayList<>();
        List<Firma> firme = firmaRepository.findAll();
        for (Firma f : firme) {
            FirmaDTO dto = firmaMapper.firmaToFirmaDTO(f);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Firma kreirajFirmu(NovaFirmaDTO firmaDTO) {
        Firma f = firmaMapper.novaFirmaDTOToFirma(firmaDTO);
        return this.firmaRepository.save(f);
    }

    @Override
    public boolean deaktiviraj(Long brojRacuna) {
        String  vrsta = nadjiVrstuRacuna(brojRacuna);
        switch (vrsta){
            case "PravniRacun" -> {
                PravniRacun pravniRacun = nadjiAktivanPravniRacunPoBrojuRacuna(brojRacuna);
                pravniRacun.setAktivan(false);
                pravniRacunRepository.save(pravniRacun);
                return true;
            }
            case "DevizniRacun" -> {
                DevizniRacun devizniRacun = nadjiAktivanDevizniRacunPoBrojuRacuna(brojRacuna);
                devizniRacun.setAktivan(false);
                devizniRacunRepository.save(devizniRacun);
                return true;
            }
            case "TekuciRacun" -> {
                TekuciRacun tekuciRacun = nadjiAktivanTekuciRacunPoBrojuRacuna(brojRacuna);
                tekuciRacun.setAktivan(false);
                tekuciRacunRepository.save(tekuciRacun);
                return true;
            }
        }
        return false;
    }

    @Override
    public Long nadjiKorisnikaPoBrojuRacuna(Long brojRacuna) {
        Long userId;
        userId = tekuciRacunRepository.findVlasnikByBrojRacuna(brojRacuna);

        if(userId != null){
            return userId;
        }

        userId = devizniRacunRepository.findVlasnikByBrojRacuna(brojRacuna);
        return userId;
    }

    public Object nadjiRacunPoBrojuRacuna(Long brojRacuna){
        TekuciRacun tekuciRacun = nadjiAktivanTekuciRacunPoBrojuRacuna(brojRacuna);
        if(tekuciRacun != null){
            return tekuciRacun;
        }

        DevizniRacun devizniRacun = nadjiAktivanDevizniRacunPoBrojuRacuna(brojRacuna);
        if(devizniRacun != null){
            return devizniRacun;
        }

        return null;
    }

    @Override
    public boolean bankomat(Long brojRacuna, BigDecimal stanje) {
        Racun racun = racunRepository.findByBrojRacuna(brojRacuna).orElseThrow(()->
                new BankAccountNotFoundException("Racun sa brojem " + brojRacuna + " ne postoji!"));
        if(racun.getRaspolozivoStanje().add(stanje).compareTo(BigDecimal.ZERO) < 0) return false;
        racun.setStanje(racun.getStanje().add(stanje));
        racun.setRaspolozivoStanje(racun.getRaspolozivoStanje().add(stanje));
        racunRepository.save(racun);
        return true;
    }
}
