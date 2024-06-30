package rs.edu.raf.service.transaction.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rs.edu.raf.exceptions.BankAccountNotFoundException;
import rs.edu.raf.model.dto.racun.*;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.Firma;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.model.mapper.racun.FirmaMapper;
import rs.edu.raf.model.mapper.racun.RacunMapper;
import rs.edu.raf.repository.racun.FirmaRepository;
import rs.edu.raf.repository.transaction.DevizniRacunRepository;
import rs.edu.raf.repository.transaction.PravniRacunRepository;
import rs.edu.raf.repository.transaction.RacunRepository;
import rs.edu.raf.repository.transaction.TekuciRacunRepository;
import rs.edu.raf.service.racun.impl.RacunServisImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@RunWith(MockitoJUnitRunner.class)
public class RacunServisImplTests {

    @InjectMocks
    static RacunServisImpl rs;

    @Mock
    static DevizniRacunRepository devizniRacunRepository;
    @Mock
    static PravniRacunRepository pravniRacunRepository;
    @Mock
    static TekuciRacunRepository tekuciRacunRepository;
    @Mock
    static RacunRepository racunRepository;

    @Mock
    static FirmaRepository firmaRepository;

    @Mock
    static RacunMapper racunMapper;
    @Mock
    static FirmaMapper firmaMapper;
/*
    @Test
    public void kreirajDevizniRacunTest() {
        NoviDevizniRacunDTO dto = kreirajNoviDevizniRacunDTO();
        DevizniRacun dr = kreirajNoviDevizniRacun();

        given(racunMapper.noviDevizniRacunDTOToDevizniRacun(dto)).willReturn(dr);
        given(devizniRacunRepository.save(dr)).willReturn(dr);

    }*/

    @Test
    public void kreirajPravniRacunTest() {
        Firma f = kreirajFirmu();
        NoviPravniRacunDTO dto = kreirajNoviPravniRacunDTO();
        PravniRacun pr = kreirajPravniRacun();

        given(racunMapper.noviPravniRacunDTOToPravniRacun(dto)).willReturn(pr);
        given(pravniRacunRepository.save(pr)).willReturn(pr);
        given(firmaRepository.findById(pr.getVlasnik())).willReturn(Optional.of(f));

        PravniRacun prRS = rs.kreirajPravniRacun(dto);
        assertEquals(pr, prRS);
    }/*

    @Test
    public void kreirajTekuciRacunTest() {
        NoviTekuciRacunDTO dto = kreirajNoviTekuciRacunDTO();
        TekuciRacun tr = kreirajTekuciRacun();

        given(racunMapper.noviTekuciRacunDTOToTekuciRacun(dto)).willReturn(tr);
        given(tekuciRacunRepository.save(tr)).willReturn(tr);

        //TekuciRacun trRS = rs.kreirajTekuciRacun(dto);
        //assertEquals(tr, trRS);
    }

    @Test
    public void izlistavanjeRacunaJednogKorisnikaTest() {
        //Korisnik k = kreirajKorisnika();
        DevizniRacun dr = kreirajNoviDevizniRacun();
        TekuciRacun tr = kreirajTekuciRacun();
        List<RacunDTO> dtos = kreirajRacunDTOs();

        //given(korisnikRepository.findById(k.getId())).willReturn(Optional.of(k));
        given(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr.getBrojRacuna())).willReturn(Optional.of(dr));
        given(racunMapper.devizniRacunToRacunDTO(dr)).willReturn(dtos.get(0));
        given(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr.getBrojRacuna())).willReturn(Optional.of(tr));
        given(racunMapper.tekuciRacunToRacunDTO(tr)).willReturn(dtos.get(1));

        //List<RacunDTO> dtosRS = rs.izlistavanjeRacunaJednogKorisnika(k.getId());
        //assertEquals(dtos, dtosRS);
    }*/

    @Test
    public void izlistavanjeRacunaJedneFirmeTest() {
        Firma f = kreirajFirmu();
        PravniRacun pr = kreirajPravniRacun();
        List<RacunDTO> dtos = kreirajPRacunDTOs();

        given(firmaRepository.findById(f.getId())).willReturn(Optional.of(f));
        given(pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(pr.getBrojRacuna())).willReturn(Optional.of(pr));
        given(racunMapper.pravniRacunToRacunDTO(pr)).willReturn(dtos.get(0));

        List<RacunDTO> dtosRS = rs.izlistavanjeRacunaJedneFirme(f.getId());
        assertEquals(dtos, dtosRS);
    }
    @Test
    public void nadjiAktivanRacunPoIDTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        PravniRacun pr = kreirajPravniRacun();
        TekuciRacun tr = kreirajTekuciRacun();
        RacunDTO ddto = kreirajRacunDTOs().get(0);
        RacunDTO pdto = kreirajPRacunDTOs().get(0);
        RacunDTO tdto = kreirajRacunDTOs().get(1);

        given(racunMapper.devizniRacunToRacunDTO(dr)).willReturn(ddto);
        given(racunMapper.pravniRacunToRacunDTO(pr)).willReturn(pdto);
        given(racunMapper.tekuciRacunToRacunDTO(tr)).willReturn(tdto);

        given(devizniRacunRepository.findByIdAndAktivanIsTrue(dr.getId())).willReturn(Optional.of(dr));
        RacunDTO dto = rs.nadjiAktivanRacunPoID(dr.getId());
        assertEquals(ddto, dto);

        given(devizniRacunRepository.findByIdAndAktivanIsTrue(dr.getId())).willReturn(Optional.empty());
        given(pravniRacunRepository.findByIdAndAktivanIsTrue(pr.getId())).willReturn(Optional.of(pr));
        dto = rs.nadjiAktivanRacunPoID(dr.getId());
        assertEquals(pdto, dto);

        given(pravniRacunRepository.findByIdAndAktivanIsTrue(pr.getId())).willReturn(Optional.empty());
        given(tekuciRacunRepository.findByIdAndAktivanIsTrue(tr.getId())).willReturn(Optional.of(tr));
        dto = rs.nadjiAktivanRacunPoID(tr.getId());
        assertEquals(tdto, dto);

        given(tekuciRacunRepository.findByIdAndAktivanIsTrue(tr.getId())).willReturn(Optional.empty());
        BankAccountNotFoundException e = assertThrows(BankAccountNotFoundException.class, () -> rs.nadjiAktivanRacunPoID(tr.getId()));
        assertEquals(e.getMessage(), "Bank account " + tr.getId() + " not found!");
    }

    @Test
    public void nadjiAktivanDevizniRacunPoIDTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        given(devizniRacunRepository.findByIdAndAktivanIsTrue(dr.getId())).willReturn(Optional.of(dr));
        DevizniRacun drRS = rs.nadjiAktivanDevizniRacunPoID(dr.getId());
        assertEquals(dr, drRS);
    }

    @Test
    public void nadjiAktivanPravniRacunPoIDTest() {
        PravniRacun pr = kreirajPravniRacun();
        given(pravniRacunRepository.findByIdAndAktivanIsTrue(pr.getId())).willReturn(Optional.of(pr));
        PravniRacun prRS = rs.nadjiAktivanPravniRacunPoID(pr.getId());
        assertEquals(pr, prRS);
    }

    @Test
    public void nadjiAktivanTekuciRacunPoIDTest() {
        TekuciRacun tr = kreirajTekuciRacun();
        given(tekuciRacunRepository.findByIdAndAktivanIsTrue(tr.getId())).willReturn(Optional.of(tr));
        TekuciRacun trRS = rs.nadjiAktivanTekuciRacunPoID(tr.getId());
        assertEquals(tr, trRS);
    }

    @Test
    public void nadjiAktivanRacunPoBrojuRacunaTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        PravniRacun pr = kreirajPravniRacun();
        TekuciRacun tr = kreirajTekuciRacun();
        RacunDTO ddto = kreirajRacunDTOs().get(0);
        RacunDTO pdto = kreirajPRacunDTOs().get(0);
        RacunDTO tdto = kreirajRacunDTOs().get(1);

        given(racunMapper.devizniRacunToRacunDTO(dr)).willReturn(ddto);
        given(racunMapper.pravniRacunToRacunDTO(pr)).willReturn(pdto);
        given(racunMapper.tekuciRacunToRacunDTO(tr)).willReturn(tdto);
        given(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr.getBrojRacuna())).willReturn(Optional.of(dr));
        given(pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(pr.getBrojRacuna())).willReturn(Optional.of(pr));
        given(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr.getBrojRacuna())).willReturn(Optional.of(tr));

        RacunDTO dto = rs.nadjiAktivanRacunPoBrojuRacuna(dr.getBrojRacuna());
        assertEquals(ddto, dto);

        dto = rs.nadjiAktivanRacunPoBrojuRacuna(pr.getBrojRacuna());
        assertEquals(pdto, dto);

        dto = rs.nadjiAktivanRacunPoBrojuRacuna(tr.getBrojRacuna());
        assertEquals(tdto, dto);

        dto = rs.nadjiAktivanRacunPoBrojuRacuna(4100L);
        assertNull(dto);
    }

    @Test
    public void nadjiAktivanDevizniRacunPoBrojuRacunaTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        given(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr.getBrojRacuna())).willReturn(Optional.of(dr));
        DevizniRacun drRS = rs.nadjiAktivanDevizniRacunPoBrojuRacuna(dr.getBrojRacuna());
        assertEquals(dr, drRS);
    }

    @Test
    public void nadjiAktivanPravniRacunPoBrojuRacunaTest() {
        PravniRacun pr = kreirajPravniRacun();
        given(pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(pr.getBrojRacuna())).willReturn(Optional.of(pr));
        PravniRacun prRS = rs.nadjiAktivanPravniRacunPoBrojuRacuna(pr.getBrojRacuna());
        assertEquals(pr, prRS);
    }

    @Test
    public void nadjiAktivanTekuciRacunPoBrojuRacunaTest() {
        TekuciRacun tr = kreirajTekuciRacun();
        given(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr.getBrojRacuna())).willReturn(Optional.of(tr));
        TekuciRacun trRS = rs.nadjiAktivanTekuciRacunPoBrojuRacuna(tr.getBrojRacuna());
        assertEquals(tr, trRS);
    }
/*
    @Test
    public void dodajDevizniRacunKorisnikuTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        Korisnik k = kreirajKorisnika();

        assertFalse(rs.dodajDevizniRacunKorisniku(dr, k));
        dr.setBrojRacuna(444000000000011211L);
        assertTrue(rs.dodajDevizniRacunKorisniku(dr, k));
    }*/

    @Test
    public void dodajPravniRacunFirmiTest() {
        PravniRacun pr = kreirajPravniRacun();
        Firma f = kreirajFirmu();
        Firma fp = kreirajFirmuPraznu();

        assertTrue(rs.dodajPravniRacunFirmi(pr, fp));
        assertFalse(rs.dodajPravniRacunFirmi(pr, f));
        pr.setBrojRacuna(444000000000011222L);
        assertTrue(rs.dodajPravniRacunFirmi(pr, f));
    }/*

    @Test
    public void dodajTekuciRacunKorisnikuTest() {
        TekuciRacun tr = kreirajTekuciRacun();
        Korisnik k = kreirajKorisnika();

        assertFalse(rs.dodajTekuciRacunKorisniku(tr, k));
        tr.setBrojRacuna(444000000000011233L);
        assertTrue(rs.dodajTekuciRacunKorisniku(tr, k));
    }


 */
    @Test
    public void generisiBrojRacunaTest() {
        given(devizniRacunRepository.findTop1ByOrderByIdDesc()).willReturn(111L);
        given(pravniRacunRepository.findTop1ByOrderByIdDesc()).willReturn(111L);
        given(tekuciRacunRepository.findTop1ByOrderByIdDesc()).willReturn(111L);
        assertEquals(444000000000011111L, (long) rs.generisiBrojRacuna("DevizniRacun"));
        assertEquals(444000000000011122L, (long) rs.generisiBrojRacuna("PravniRacun"));
        assertEquals(444000000000011133L, (long) rs.generisiBrojRacuna("TekuciRacun"));
        assertNull(rs.generisiBrojRacuna("Racun"));
    }

    @Test
    public void nadjiVrstuRacunaTest() {
        assertEquals("DevizniRacun", rs.nadjiVrstuRacuna(444000000000011111L));
        assertEquals("PravniRacun", rs.nadjiVrstuRacuna(444000000000011122L));
        assertEquals("TekuciRacun", rs.nadjiVrstuRacuna(444000000000011133L));
        assertNull(rs.nadjiVrstuRacuna(4100L));
    }

    @Test
    public void izlistajSveFirmeTest() {
        Firma f = kreirajFirmu();
        List<Firma> fs = List.of(f);
        List<FirmaDTO> dtos = kreirajFirmaDTOs();

        given(firmaRepository.findAll()).willReturn(fs);
        given(firmaMapper.firmaToFirmaDTO(f)).willReturn(dtos.get(0));
        List<FirmaDTO> dtosRS = rs.izlistajSveFirme();
        assertEquals(dtos, dtosRS);
    }

    @Test
    public void dohvatiFirmuTest() {
        Firma f = kreirajFirmu();
        FirmaDTO dto = kreirajFirmaDTOs().get(0);

        given(firmaRepository.findById(f.getId())).willReturn(Optional.of(f));
        given(firmaMapper.firmaToFirmaDTO(f)).willReturn(dto);

        FirmaDTO dtoRS = rs.dohvatiFirmu(f.getId());
        assertEquals(dto, dtoRS);

        given(firmaRepository.findById(f.getId())).willReturn(Optional.empty());
        assertNull(rs.dohvatiFirmu(f.getId()));
    }

    @Test
    public void kreirajFirmuTest() {
        Firma f = kreirajFirmu();
        NovaFirmaDTO dto = kreirajNovaFirmaDTO();

        given(firmaMapper.novaFirmaDTOToFirma(dto)).willReturn(f);
        given(firmaRepository.save(f)).willReturn(f);

        Firma fRS = rs.kreirajFirmu(dto);
        assertEquals(f, fRS);
    }

    @Test
    public void deaktivirajTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        given(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr.getBrojRacuna())).willReturn(Optional.of(dr));
        PravniRacun pr = kreirajPravniRacun();
        given(pravniRacunRepository.findByBrojRacunaAndAktivanIsTrue(pr.getBrojRacuna())).willReturn(Optional.of(pr));
        TekuciRacun tr = kreirajTekuciRacun();
        given(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr.getBrojRacuna())).willReturn(Optional.of(tr));

        assertTrue(rs.deaktiviraj(dr.getBrojRacuna()));
        assertTrue(rs.deaktiviraj(pr.getBrojRacuna()));
        assertTrue(rs.deaktiviraj(tr.getBrojRacuna()));

        assertFalse(rs.deaktiviraj(4100L));
    }

    @Test
    public void nadjiKorisnikaPoBrojuRacunaTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        TekuciRacun tr = kreirajTekuciRacun();

        given(tekuciRacunRepository.findVlasnikByBrojRacuna(tr.getBrojRacuna())).willReturn(tr.getVlasnik());
        Long userId = rs.nadjiKorisnikaPoBrojuRacuna(tr.getBrojRacuna());
        assertEquals(userId, tr.getVlasnik());

        given(devizniRacunRepository.findVlasnikByBrojRacuna(dr.getBrojRacuna())).willReturn(dr.getVlasnik());
        given(tekuciRacunRepository.findVlasnikByBrojRacuna(dr.getBrojRacuna())).willReturn(null);
        userId = rs.nadjiKorisnikaPoBrojuRacuna(dr.getBrojRacuna());
        assertEquals(userId, dr.getVlasnik());
    }

    @Test
    public void nadjiRacunPoBrojuRacunaTest() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        TekuciRacun tr = kreirajTekuciRacun();
        given(tekuciRacunRepository.findByBrojRacunaAndAktivanIsTrue(tr.getBrojRacuna())).willReturn(Optional.of(tr));
        given(devizniRacunRepository.findByBrojRacunaAndAktivanIsTrue(dr.getBrojRacuna())).willReturn(Optional.of(dr));

        Object racun = rs.nadjiRacunPoBrojuRacuna(tr.getBrojRacuna());
        assertEquals(racun, tr);

        racun = rs.nadjiRacunPoBrojuRacuna(dr.getBrojRacuna());
        assertEquals(racun, dr);

        assertNull(rs.nadjiRacunPoBrojuRacuna(4100L));
    }

    @Test
    public void bankomat() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        given(racunRepository.findByBrojRacuna(dr.getBrojRacuna())).willReturn(Optional.of(dr));
        assertTrue(rs.bankomat(dr.getBrojRacuna(), BigDecimal.valueOf(100)));
    }

    private NoviDevizniRacunDTO kreirajNoviDevizniRacunDTO() {
        NoviDevizniRacunDTO dto = new NoviDevizniRacunDTO();
        dto.setVlasnik(11111L);
        dto.setZaposleni(22222L);
        dto.setCurrency(List.of("Srpski dinar", "Americki dolar"));
        dto.setDefaultCurrency("Srpski dinar");
        dto.setBrojDozvoljenihValuta(2);
        return dto;
    }

    private DevizniRacun kreirajNoviDevizniRacun() {
        DevizniRacun dr = new DevizniRacun();
        dr.setBrojRacuna(444000000000011111L);
        dr.setVlasnik(11111L);
        dr.setStanje(new BigDecimal("0"));
        dr.setRaspolozivoStanje(new BigDecimal("0"));
        dr.setZaposleni(22222L);
        dr.setCurrency("Srpski dinar,Americki dolar");
        dr.setAktivan(true);
        dr.setKamatnaStopa(new BigDecimal("1"));
        dr.setOdrzavanjeRacuna(new BigDecimal("200"));
        return dr;
    }

    private NoviPravniRacunDTO kreirajNoviPravniRacunDTO() {
        NoviPravniRacunDTO dto = new NoviPravniRacunDTO();
        dto.setFirma(11111L);
        dto.setZaposleni(22222L);
        return dto;
    }

    private PravniRacun kreirajPravniRacun() {
        PravniRacun pr = new PravniRacun();
        pr.setBrojRacuna(444000000000011122L);
        pr.setVlasnik(11111L);
        pr.setStanje(new BigDecimal("0"));
        pr.setRaspolozivoStanje(new BigDecimal("0"));
        pr.setZaposleni(22222L);
        pr.setCurrency("Srpski dinar");
        pr.setAktivan(true);
        return pr;
    }

    private NoviTekuciRacunDTO kreirajNoviTekuciRacunDTO() {
        NoviTekuciRacunDTO dto = new NoviTekuciRacunDTO();
        dto.setVlasnik(11111L);
        dto.setZaposleni(22222L);
        dto.setVrstaRacuna("Studentski");
        return dto;
    }

    private TekuciRacun kreirajTekuciRacun() {
        TekuciRacun tr = new TekuciRacun();
        tr.setBrojRacuna(444000000000011133L);
        tr.setVlasnik(11111L);
        tr.setStanje(new BigDecimal("0"));
        tr.setRaspolozivoStanje(new BigDecimal("0"));
        tr.setZaposleni(22222L);
        tr.setCurrency("Srpski dinar");
        tr.setAktivan(true);
        tr.setVrstaRacuna("Studentski");
        tr.setOdrzavanjeRacuna(new BigDecimal("0"));
        return tr;
    }
    /*

    private Korisnik kreirajKorisnika() { //pozajmljeno :)
        Korisnik k = new Korisnik();
        k.setId(0L);
        k.setIme("Pera");
        k.setPrezime("Peric");
        k.setJmbg(1705000793457L);
        k.setDatumRodjenja(958514400000L);
        k.setPol("M");
        k.setEmail("pera.petrovic@gmail.com");
        k.setBrojTelefona("0641234567");
        k.setAdresa("Bulevar Kralja Petra 1");
        k.setPassword("pera123");
        k.setPovezaniRacuni("444000000000011111,444000000000011133");
        k.setAktivan(true);

        return k;
    }

     */

    private List<RacunDTO> kreirajRacunDTOs() {
        DevizniRacun dr = kreirajNoviDevizniRacun();
        RacunDTO dto1 = new RacunDTO();

        dto1.setId(dr.getId());
        dto1.setBrojRacuna(dr.getBrojRacuna().toString());
        dto1.setVlasnik(dr.getVlasnik());
        dto1.setStanje(dr.getStanje());
        dto1.setRaspolozivoStanje(dr.getRaspolozivoStanje());
        dto1.setZaposleni(dr.getZaposleni());
        dto1.setDatumKreiranja(dr.getDatumKreiranja());
        dto1.setDatumIsteka(dr.getDatumIsteka());
        dto1.setCurrency(dr.getCurrency());
        dto1.setAktivan(dr.getAktivan());

        TekuciRacun tk = kreirajTekuciRacun();
        RacunDTO dto2 = new RacunDTO();
        dto2.setId(tk.getId());
        dto2.setBrojRacuna(tk.getBrojRacuna().toString());
        dto2.setVlasnik(tk.getVlasnik());
        dto2.setStanje(tk.getStanje());
        dto2.setRaspolozivoStanje(tk.getRaspolozivoStanje());
        dto2.setZaposleni(tk.getZaposleni());
        dto2.setDatumKreiranja(tk.getDatumKreiranja());
        dto2.setDatumIsteka(tk.getDatumIsteka());
        dto2.setCurrency(tk.getCurrency());
        dto2.setAktivan(tk.getAktivan());

        return List.of(dto1, dto2);
    }

    private Firma kreirajFirmu() {
        Firma f = new Firma(11222L, "Belit d.o.o. Beograd", "444000000000011122", "0112030403", "0112030402", 101017533, 17328905, 6102, 130501701);
        return f;
    }

    private Firma kreirajFirmuPraznu() {
        Firma f = new Firma(11222L, "Belit d.o.o. Beograd", null, "0112030403", "0112030402", 101017533, 17328905, 6102, 130501701);
        return f;
    }

    private List<RacunDTO> kreirajPRacunDTOs() {
        PravniRacun pr = kreirajPravniRacun();
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

        return List.of(dto);
    }

    private List<FirmaDTO> kreirajFirmaDTOs() {
        List<FirmaDTO> dtos = new ArrayList<>();
        Firma f = kreirajFirmu();
        FirmaDTO dto = new FirmaDTO();
        dto.setId(f.getId());
        dto.setNazivPreduzeca(f.getNazivPreduzeca());
        dto.setPovezaniRacuni(f.getPovezaniRacuni());
        dto.setBrojTelefona(f.getBrojTelefona());
        dto.setBrojFaksa(f.getBrojFaksa());
        dto.setPIB(f.getPIB());
        dto.setMaticniBroj(f.getMaticniBroj());
        dto.setSifraDelatnosti(f.getSifraDelatnosti());
        dto.setRegistarskiBroj(f.getRegistarskiBroj());
        dtos.add(dto);
        return dtos;
    }

    private NovaFirmaDTO kreirajNovaFirmaDTO() {
        NovaFirmaDTO dto = new NovaFirmaDTO();
        dto.setNazivPreduzeca("Belit d.o.o. Beograd");
        dto.setBrojTelefona("0112030403");
        dto.setBrojFaksa("0112030404");
        dto.setPIB(101017533);
        dto.setMaticniBroj(17328905);
        dto.setSifraDelatnosti(6102);
        dto.setRegistarskiBroj(130501701);
        return dto;
    }


}
