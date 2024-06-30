package rs.edu.raf.racun.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rs.edu.raf.model.dto.racun.NoviDevizniRacunDTO;
import rs.edu.raf.model.dto.racun.NoviPravniRacunDTO;
import rs.edu.raf.model.dto.racun.NoviTekuciRacunDTO;
import rs.edu.raf.model.dto.racun.RacunDTO;
import rs.edu.raf.model.mapper.racun.RacunMapper;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.repository.transaction.DevizniRacunRepository;
import rs.edu.raf.repository.transaction.PravniRacunRepository;
import rs.edu.raf.repository.transaction.TekuciRacunRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class RacunMapperTests {
    @InjectMocks
    static RacunMapper rm;

    @Mock
    static DevizniRacunRepository drr;

    @Mock
    static PravniRacunRepository prr;

    @Mock
    static TekuciRacunRepository trr;

    @Test
    public void noviDevizniRacunDTOToDevizniRacunTest() {
        NoviDevizniRacunDTO dto = new NoviDevizniRacunDTO();
        dto.setVlasnik(11111L);
        dto.setZaposleni(22222L);
        dto.setCurrency(List.of("RSD", "Americki dolar"));
        dto.setDefaultCurrency("RSD");
        dto.setBrojDozvoljenihValuta(2);

        DevizniRacun dr = new DevizniRacun();
        dr.setBrojRacuna(444000000000011111L);
        dr.setVlasnik(11111L);
        dr.setStanje(new BigDecimal("0"));
        dr.setRaspolozivoStanje(new BigDecimal("0"));
        dr.setZaposleni(22222L);
        dr.setCurrency("RSD,Americki dolar");
        dr.setAktivan(true);
        dr.setKamatnaStopa(new BigDecimal("1"));
        dr.setOdrzavanjeRacuna(new BigDecimal("200"));

        given(drr.findTop1ByOrderByIdDesc()).willReturn(111L);
        DevizniRacun drRM = rm.noviDevizniRacunDTOToDevizniRacun(dto);

        System.out.println(drRM.getId());
        assertEquals(dr.getBrojRacuna(), drRM.getBrojRacuna());
        assertEquals(dr.getVlasnik(), drRM.getVlasnik());
        assertEquals(dr.getStanje(), drRM.getStanje());
        assertEquals(dr.getRaspolozivoStanje(), drRM.getRaspolozivoStanje());
        assertEquals(dr.getZaposleni(), drRM.getZaposleni());
        System.out.println(new Date(drRM.getDatumKreiranja()));
        System.out.println(new Date(drRM.getDatumIsteka()));
        assertEquals(dr.getCurrency(), drRM.getCurrency());
        assertEquals(dr.getAktivan(), drRM.getAktivan());
        assertEquals(dr.getKamatnaStopa(), drRM.getKamatnaStopa());
        assertEquals(dr.getOdrzavanjeRacuna(), drRM.getOdrzavanjeRacuna());
    }

    @Test
    public void noviPravniRacunDTOToPravniRacunTest() {
        NoviPravniRacunDTO dto = new NoviPravniRacunDTO();
        dto.setFirma(11111L);
        dto.setZaposleni(22222L);

        PravniRacun pr = new PravniRacun();
        pr.setBrojRacuna(444000000000011122L);
        pr.setVlasnik(11111L);
        pr.setStanje(new BigDecimal("0"));
        pr.setRaspolozivoStanje(new BigDecimal("0"));
        pr.setZaposleni(22222L);
        pr.setCurrency("RSD");
        pr.setAktivan(true);

        given(prr.findTop1ByOrderByIdDesc()).willReturn(111L);
        PravniRacun prRM = rm.noviPravniRacunDTOToPravniRacun(dto);

        System.out.println(prRM.getId());
        assertEquals(pr.getBrojRacuna(), prRM.getBrojRacuna());
        assertEquals(pr.getVlasnik(), prRM.getVlasnik());
        assertEquals(pr.getStanje(), prRM.getStanje());
        assertEquals(pr.getRaspolozivoStanje(), prRM.getRaspolozivoStanje());
        assertEquals(pr.getZaposleni(), prRM.getZaposleni());
        System.out.println(new Date(prRM.getDatumKreiranja()));
        System.out.println(new Date(prRM.getDatumIsteka()));
        assertEquals(pr.getCurrency(), prRM.getCurrency());
        assertEquals(pr.getAktivan(), prRM.getAktivan());
    }

    @Test
    public void noviTekuciRacunDTOToTekuciRacunTest() {
        NoviTekuciRacunDTO dto = new NoviTekuciRacunDTO();
        dto.setVlasnik(11111L);
        dto.setZaposleni(22222L);
        dto.setVrstaRacuna("Studentski");

        TekuciRacun tr = new TekuciRacun();
        tr.setBrojRacuna(444000000000011133L);
        tr.setVlasnik(11111L);
        tr.setStanje(new BigDecimal("0"));
        tr.setRaspolozivoStanje(new BigDecimal("0"));
        tr.setZaposleni(22222L);
        tr.setCurrency("RSD");
        tr.setAktivan(true);
        tr.setVrstaRacuna("Studentski");
        tr.setOdrzavanjeRacuna(new BigDecimal("0"));

        given(trr.findTop1ByOrderByIdDesc()).willReturn(111L);
        TekuciRacun trRM = rm.noviTekuciRacunDTOToTekuciRacun(dto);

        System.out.println(trRM.getId());
        assertEquals(tr.getBrojRacuna(), trRM.getBrojRacuna());
        assertEquals(tr.getVlasnik(), trRM.getVlasnik());
        assertEquals(tr.getStanje(), trRM.getStanje());
        assertEquals(tr.getRaspolozivoStanje(), trRM.getRaspolozivoStanje());
        assertEquals(tr.getZaposleni(), trRM.getZaposleni());
        System.out.println(new Date(trRM.getDatumKreiranja()));
        System.out.println(new Date(trRM.getDatumIsteka()));
        assertEquals(tr.getCurrency(), trRM.getCurrency());
        assertEquals(tr.getAktivan(), trRM.getAktivan());
        assertEquals(tr.getVrstaRacuna(), trRM.getVrstaRacuna());
        assertEquals(tr.getOdrzavanjeRacuna(), trRM.getOdrzavanjeRacuna());
    }

    @Test
    public void DevizniRacunToRacunDTOTest() {

        DevizniRacun dr = new DevizniRacun();
        dr.setId(112L);
        dr.setBrojRacuna(444000000000011111L);
        dr.setVlasnik(11111L);
        dr.setStanje(new BigDecimal("0"));
        dr.setRaspolozivoStanje(new BigDecimal("0"));
        dr.setZaposleni(22222L);
        dr.setDatumKreiranja(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000L);
        dr.setDatumIsteka(dr.getDatumKreiranja() + 5*1000L*31536000L);
        dr.setCurrency("RSD,Americki dolar");
        dr.setAktivan(true);

        RacunDTO dto = rm.devizniRacunToRacunDTO(dr);

        assertEquals(dto.getId(), dr.getId());
        assertEquals(dto.getBrojRacuna(), dr.getBrojRacuna().toString());
        assertEquals(dto.getVlasnik(), dr.getVlasnik());
        assertEquals(dto.getStanje(), dr.getStanje());
        assertEquals(dto.getRaspolozivoStanje(), dr.getRaspolozivoStanje());
        assertEquals(dto.getZaposleni(), dr.getZaposleni());
        assertEquals(dto.getDatumKreiranja(), dr.getDatumKreiranja());
        assertEquals(dto.getDatumIsteka(), dr.getDatumIsteka());
        assertEquals(dto.getCurrency(), dr.getCurrency());
        assertEquals(dto.getAktivan(), dr.getAktivan());
    }

    public void PravniRacunToRacunDTOTest() {

        PravniRacun pr = new PravniRacun();
        pr.setId(112L);
        pr.setBrojRacuna(444000000000011122L);
        pr.setVlasnik(11111L);
        pr.setStanje(new BigDecimal("0"));
        pr.setRaspolozivoStanje(new BigDecimal("0"));
        pr.setZaposleni(22222L);
        pr.setDatumKreiranja(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000L);
        pr.setDatumIsteka(pr.getDatumKreiranja() + 5*1000L*31536000L);
        pr.setCurrency("RSD");
        pr.setAktivan(true);

        RacunDTO dto = rm.pravniRacunToRacunDTO(pr);

        assertEquals(dto.getId(), pr.getId());
        assertEquals(dto.getBrojRacuna(), pr.getBrojRacuna());
        assertEquals(dto.getVlasnik(), pr.getVlasnik());
        assertEquals(dto.getStanje(), pr.getStanje());
        assertEquals(dto.getRaspolozivoStanje(), pr.getRaspolozivoStanje());
        assertEquals(dto.getZaposleni(), pr.getZaposleni());
        assertEquals(dto.getDatumKreiranja(), pr.getDatumKreiranja());
        assertEquals(dto.getDatumIsteka(), pr.getDatumIsteka());
        assertEquals(dto.getCurrency(), pr.getCurrency());
        assertEquals(dto.getAktivan(), pr.getAktivan());
    }

    public void TekuciRacunToRacunDTOTest() {

        TekuciRacun tr = new TekuciRacun();
        tr.setId(112L);
        tr.setBrojRacuna(444000000000011133L);
        tr.setVlasnik(11111L);
        tr.setStanje(new BigDecimal("0"));
        tr.setRaspolozivoStanje(new BigDecimal("0"));
        tr.setZaposleni(22222L);
        tr.setDatumKreiranja(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) * 1000L);
        tr.setDatumIsteka(tr.getDatumKreiranja() + 5*1000L*31536000L);
        tr.setCurrency("RSD");
        tr.setAktivan(true);

        RacunDTO dto = rm.tekuciRacunToRacunDTO(tr);

        assertEquals(dto.getId(), tr.getId());
        assertEquals(dto.getBrojRacuna(), tr.getBrojRacuna());
        assertEquals(dto.getVlasnik(), tr.getVlasnik());
        assertEquals(dto.getStanje(), tr.getStanje());
        assertEquals(dto.getRaspolozivoStanje(), tr.getRaspolozivoStanje());
        assertEquals(dto.getZaposleni(), tr.getZaposleni());
        assertEquals(dto.getDatumKreiranja(), tr.getDatumKreiranja());
        assertEquals(dto.getDatumIsteka(), tr.getDatumIsteka());
        assertEquals(dto.getCurrency(), tr.getCurrency());
        assertEquals(dto.getAktivan(), tr.getAktivan());
    }
}
