package rs.edu.raf.servis.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.repository.transaction.*;
import rs.edu.raf.service.ExchangeRateServiceImpl;
import rs.edu.raf.service.racun.RacunServis;
import rs.edu.raf.model.dto.transaction.NovaUplataDTO;
import rs.edu.raf.model.dto.transaction.NoviPrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.PrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.UplataDTO;
import rs.edu.raf.model.entities.transaction.PrenosSredstava;
import rs.edu.raf.model.entities.transaction.Status;
import rs.edu.raf.model.entities.transaction.Uplata;
import rs.edu.raf.service.transaction.impl.TransakcijaServisImpl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransakcijaServisImplTest {

    @Mock
    private UplataRepository uplataRepository;

    @Mock
    private PrenosSredstavaRepository prenosSredstavaRepository;

    @Mock
    private RacunServis racunServis;

    @Mock
    private PravniRacunRepository pravniRacunRepository;

    @Mock
    private TekuciRacunRepository tekuciRacunRepository;

    @Mock
    private DevizniRacunRepository devizniRacunRepository;

    @InjectMocks
    private TransakcijaServisImpl transakcijaServis;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transakcijaServis = Mockito.spy(new TransakcijaServisImpl(
                uplataRepository,
                prenosSredstavaRepository,
                Mockito.mock(SablonTransakcijeRepository.class),
                pravniRacunRepository,
                tekuciRacunRepository,
                devizniRacunRepository,
                Mockito.mock(ExchangeRateServiceImpl.class),
                racunServis,
                Mockito.mock(SimpMessagingTemplate.class)
        ));
    }


    @Test
    public void givenValidDto_whenSacuvajPrenosSredstava_thenReturnExpectedValue() {
        NoviPrenosSredstavaDTO dto = new NoviPrenosSredstavaDTO();
        dto.setRacunPosiljaoca(1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("PravniRacun");
        PravniRacun pravniRacun = new PravniRacun();
        pravniRacun.setRaspolozivoStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(pravniRacun);
        PrenosSredstava expected = new PrenosSredstava();
        when(prenosSredstavaRepository.save(any(PrenosSredstava.class))).thenReturn(expected);

        PrenosSredstava result = transakcijaServis.sacuvajPrenosSredstava(dto);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidDto_whenSacuvajUplatu_thenReturnExpectedValue() {
        NovaUplataDTO dto = new NovaUplataDTO();
        dto.setRacunPosiljaoca(1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("PravniRacun");
        PravniRacun pravniRacun = new PravniRacun();
        pravniRacun.setRaspolozivoStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(pravniRacun);
        Uplata expected = new Uplata();
        when(uplataRepository.save(any(Uplata.class))).thenReturn(expected);

        Uplata result = transakcijaServis.sacuvajUplatu(dto);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidDto_whenSacuvajPrenosSredstava_thenReturnExpectedValue2() {
        NoviPrenosSredstavaDTO dto = new NoviPrenosSredstavaDTO();
        dto.setRacunPosiljaoca(1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("TekuciRacun");
        TekuciRacun tekuciRacun = new TekuciRacun();
        tekuciRacun.setRaspolozivoStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(tekuciRacun);
        PrenosSredstava expected = new PrenosSredstava();
        when(prenosSredstavaRepository.save(any(PrenosSredstava.class))).thenReturn(expected);

        PrenosSredstava result = transakcijaServis.sacuvajPrenosSredstava(dto);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidDto_whenSacuvajUplatu_thenReturnExpectedValue2() {
        NovaUplataDTO dto = new NovaUplataDTO();
        dto.setRacunPosiljaoca(1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("TekuciRacun");
        TekuciRacun tekuciRacun = new TekuciRacun();
        tekuciRacun.setRaspolozivoStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(tekuciRacun);
        Uplata expected = new Uplata();
        when(uplataRepository.save(any(Uplata.class))).thenReturn(expected);

        Uplata result = transakcijaServis.sacuvajUplatu(dto);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidDto_whenSacuvajPrenosSredstava_thenReturnExpectedValue3() {
        NoviPrenosSredstavaDTO dto = new NoviPrenosSredstavaDTO();
        dto.setRacunPosiljaoca(1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("DevizniRacun");
        DevizniRacun devizniRacun = new DevizniRacun();
        devizniRacun.setRaspolozivoStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(devizniRacun);
        PrenosSredstava expected = new PrenosSredstava();
        when(prenosSredstavaRepository.save(any(PrenosSredstava.class))).thenReturn(expected);

        PrenosSredstava result = transakcijaServis.sacuvajPrenosSredstava(dto);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidDto_whenSacuvajUplatu_thenReturnExpectedValue3() {
        NovaUplataDTO dto = new NovaUplataDTO();
        dto.setRacunPosiljaoca(1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("DevizniRacun");
        DevizniRacun devizniRacun = new DevizniRacun();
        devizniRacun.setRaspolozivoStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(devizniRacun);
        Uplata expected = new Uplata();
        when(uplataRepository.save(any(Uplata.class))).thenReturn(expected);

        Uplata result = transakcijaServis.sacuvajUplatu(dto);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidId_whenVratiPrenosSredstavaDtoPoRacunuPrimaoca_thenReturnPrenosSredstavaDTOList() {
        Long racunPrimaoca = 1L;
        List<PrenosSredstavaDTO> expected = new ArrayList<>();
        when(prenosSredstavaRepository.findAllByRacunPrimaoca(racunPrimaoca)).thenReturn(new ArrayList<>());

        List<PrenosSredstavaDTO> result = transakcijaServis.vratiPrenosSredstavaDtoPoRacunuPrimaoca(racunPrimaoca);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidId_whenVratiUplataDtoPoRacunuPrimaoca_thenReturnUplataDTOList() {
        Long racunPrimaoca = 1L;
        List<UplataDTO> expected = new ArrayList<>();
        when(uplataRepository.findAllByRacunPrimaoca(racunPrimaoca)).thenReturn(new ArrayList<>());

        List<UplataDTO> result = transakcijaServis.vratiUplataDtoPoRacunuPrimaoca(racunPrimaoca);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidId_whenVratiPrenosSredstavaDtoPoRacunuPosiljaoca_thenReturnPrenosSredstavaDTOList() {
        Long racunPosiljaoca = 1L;
        List<PrenosSredstavaDTO> expected = new ArrayList<>();
        when(prenosSredstavaRepository.findAllByRacunPosiljaoca(racunPosiljaoca)).thenReturn(new ArrayList<>());

        List<PrenosSredstavaDTO> result = transakcijaServis.vratiPrenosSredstavaDtoPoRacunuPosiljaoca(racunPosiljaoca);

        assertEquals(expected, result);
    }

    @Test
    public void givenValidId_whenVratiUplataDtoPoRacunuPosiljaoca_thenReturnUplataDTOList() {
        Long racunPosiljaoca = 1L;
        List<UplataDTO> expected = new ArrayList<>();
        when(uplataRepository.findAllByRacunPosiljaoca(racunPosiljaoca)).thenReturn(new ArrayList<>());

        List<UplataDTO> result = transakcijaServis.vratiUplataDtoPoRacunuPosiljaoca(racunPosiljaoca);

        assertEquals(expected, result);
    }

    @Test
    public void whenVratiPrenosSredstavaUObradi_thenReturnPrenosSredstavaList() {
        List<PrenosSredstava> expected = new ArrayList<>();
        when(prenosSredstavaRepository.findAllByStatus(Status.U_OBRADI)).thenReturn(new ArrayList<>());

        List<PrenosSredstava> result = transakcijaServis.vratiPrenosSredstavaUObradi();

        assertEquals(expected, result);
    }

    @Test
    public void whenVratiUplateUObradi_thenReturnUplataList() {
        List<Uplata> expected = new ArrayList<>();
        when(uplataRepository.findAllByStatus(Status.U_OBRADI)).thenReturn(new ArrayList<>());

        List<Uplata> result = transakcijaServis.vratiUplateUObradi();

        assertEquals(expected, result);
    }

    @Test
    public void givenValidIdAndStatus_whenPromeniStatusUplate_thenReturnUpdatedUplata() {
        Long idUplate = 1L;
        String newStatus = "REALIZOVANO";
        Uplata expected = new Uplata();
        expected.setId(String.valueOf(idUplate));
        expected.setStatus(newStatus);
        when(uplataRepository.findById(String.valueOf(idUplate))).thenReturn(Optional.of(new Uplata()));
        when(uplataRepository.save(any(Uplata.class))).thenReturn(expected);

        Uplata result = transakcijaServis.promeniStatusUplate(String.valueOf(idUplate), newStatus, System.currentTimeMillis());

        assertEquals(expected, result);
    }

    @Test
    public void givenValidIdAndStatus_whenPromeniStatusPrenosaSredstava_thenReturnUpdatedPrenosSredstava() {
        Long idPrenosaSredstava = 1L;
        String newStatus = "REALIZOVANO";
        PrenosSredstava expected = new PrenosSredstava();
        expected.setId(String.valueOf(idPrenosaSredstava));
        expected.setStatus(newStatus);
        when(prenosSredstavaRepository.findById(String.valueOf(idPrenosaSredstava))).thenReturn(Optional.of(new PrenosSredstava()));
        when(prenosSredstavaRepository.save(any(PrenosSredstava.class))).thenReturn(expected);

        PrenosSredstava result = transakcijaServis.promeniStatusPrenosaSredstava(String.valueOf(idPrenosaSredstava), newStatus, System.currentTimeMillis());

        assertEquals(expected, result);
    }

    /*
    @Test
    public void givenValidId_whenIzracunajRezervisanaSredstva_thenReturnExpectedValue() {
        Long idRacuna = 1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("PravniRacun");
        PravniRacun pravniRacun = new PravniRacun();
        pravniRacun.setStanje(new BigDecimal("100.00"));
        pravniRacun.setRaspolozivoStanje(new BigDecimal("50.00"));
        when(racunServis.nadjiAktivanPravniRacunPoID(idRacuna)).thenReturn(pravniRacun);

        BigDecimal result = transakcijaServis.izracunajRezervisanaSredstva(idRacuna);

        assertEquals(new BigDecimal("50.00"), result);
    }


     */
    @Test
    public void givenValidId_whenVratiSredstva_thenReturnExpectedValue() {
        Long idRacuna = 1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("PravniRacun");
        PravniRacun pravniRacun = new PravniRacun();
        pravniRacun.setStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanPravniRacunPoID(idRacuna)).thenReturn(pravniRacun);

        BigDecimal result = transakcijaServis.vratiSredstva(idRacuna);

        assertEquals(new BigDecimal("100.00"), result);
    }
/*
    @Test
    public void givenValidId_whenIzracunajRezervisanaSredstvaForTekuciRacun_thenReturnExpectedValue() {
        Long idRacuna = 1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("TekuciRacun");
        TekuciRacun tekuciRacun = new TekuciRacun();
        tekuciRacun.setStanje(new BigDecimal("100.00"));
        tekuciRacun.setRaspolozivoStanje(new BigDecimal("50.00"));
        when(racunServis.nadjiAktivanTekuciRacunPoID(idRacuna)).thenReturn(tekuciRacun);

        BigDecimal result = transakcijaServis.izracunajRezervisanaSredstva(idRacuna);

        assertEquals(new BigDecimal("50.00"), result);
    }


 */
    @Test
    public void givenValidId_whenVratiSredstvaForTekuciRacun_thenReturnExpectedValue() {
        Long idRacuna = 1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("TekuciRacun");
        TekuciRacun tekuciRacun = new TekuciRacun();
        tekuciRacun.setStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanTekuciRacunPoID(idRacuna)).thenReturn(tekuciRacun);

        BigDecimal result = transakcijaServis.vratiSredstva(idRacuna);

        assertEquals(new BigDecimal("100.00"), result);
    }
/*    }

    @Test
    public void givenValidId_whenIzracunajRezervisanaSredstvaForDevizniRacun_thenReturnExpectedValue() {
        Long idRacuna = 1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("DevizniRacun");
        DevizniRacun devizniRacun = new DevizniRacun();
        devizniRacun.setStanje(new BigDecimal("100.00"));
        devizniRacun.setRaspolozivoStanje(new BigDecimal("50.00"));
        when(racunServis.nadjiAktivanDevizniRacunPoID(idRacuna)).thenReturn(devizniRacun);

        BigDecimal result = transakcijaServis.izracunajRezervisanaSredstva(idRacuna);

        assertEquals(new BigDecimal("50.00"), result);
    }

 */

    @Test
    public void givenValidId_whenVratiSredstvaForDevizniRacun_thenReturnExpectedValue() {
        Long idRacuna = 1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("DevizniRacun");
        DevizniRacun devizniRacun = new DevizniRacun();
        devizniRacun.setStanje(new BigDecimal("100.00"));
        when(racunServis.nadjiAktivanDevizniRacunPoID(idRacuna)).thenReturn(devizniRacun);

        BigDecimal result = transakcijaServis.vratiSredstva(idRacuna);

        assertEquals(new BigDecimal("100.00"), result);
    }

    @Test
    public void givenInvalidDto_whenSacuvajPrenosSredstava_thenReturnNull() {
        NoviPrenosSredstavaDTO dto = new NoviPrenosSredstavaDTO();
        dto.setRacunPosiljaoca(-1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("PravniRacun");
        when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(null);

        PrenosSredstava result = transakcijaServis.sacuvajPrenosSredstava(dto);

        assertNull(result);
    }

    @Test
    public void givenInvalidDto_whenSacuvajUplatu_thenReturnNull() {
        NovaUplataDTO dto = new NovaUplataDTO();
        dto.setRacunPosiljaoca(-1L);
        dto.setIznos(new BigDecimal("50.00"));
        when(racunServis.nadjiVrstuRacuna(dto.getRacunPosiljaoca())).thenReturn("PravniRacun");
        when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(dto.getRacunPosiljaoca())).thenReturn(null);

        Uplata result = transakcijaServis.sacuvajUplatu(dto);

        assertNull(result);
    }

    @Test
    public void givenInvalidId_whenVratiPrenosSredstavaDtoPoRacunuPrimaoca_thenReturnEmptyList() {
        Long racunPrimaoca = -1L;
        when(prenosSredstavaRepository.findAllByRacunPrimaoca(racunPrimaoca)).thenReturn(new ArrayList<>());

        List<PrenosSredstavaDTO> result = transakcijaServis.vratiPrenosSredstavaDtoPoRacunuPrimaoca(racunPrimaoca);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenInvalidId_whenVratiUplataDtoPoRacunuPrimaoca_thenReturnEmptyList() {
        Long racunPrimaoca = -1L;
        when(uplataRepository.findAllByRacunPrimaoca(racunPrimaoca)).thenReturn(new ArrayList<>());

        List<UplataDTO> result = transakcijaServis.vratiUplataDtoPoRacunuPrimaoca(racunPrimaoca);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenInvalidId_whenVratiPrenosSredstavaDtoPoRacunuPosiljaoca_thenReturnEmptyList() {
        Long racunPosiljaoca = -1L;
        when(prenosSredstavaRepository.findAllByRacunPosiljaoca(racunPosiljaoca)).thenReturn(new ArrayList<>());

        List<PrenosSredstavaDTO> result = transakcijaServis.vratiPrenosSredstavaDtoPoRacunuPosiljaoca(racunPosiljaoca);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenInvalidId_whenVratiUplataDtoPoRacunuPosiljaoca_thenReturnEmptyList() {
        Long racunPosiljaoca = -1L;
        when(uplataRepository.findAllByRacunPosiljaoca(racunPosiljaoca)).thenReturn(new ArrayList<>());

        List<UplataDTO> result = transakcijaServis.vratiUplataDtoPoRacunuPosiljaoca(racunPosiljaoca);

        assertTrue(result.isEmpty());
    }

    @Test
    public void givenInvalidId_whenPromeniStatusUplate_thenReturnEntityNotFoundException() {
        Long idUplate = -1L;
        String newStatus = "REALIZOVANO";
        when(uplataRepository.findById(String.valueOf(idUplate))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transakcijaServis.promeniStatusUplate(String.valueOf(idUplate), newStatus, System.currentTimeMillis()));
    }

    @Test
    public void givenInvalidId_whenPromeniStatusPrenosaSredstava_thenReturnEntityNotFoundException() {
        Long idPrenosaSredstava = -1L;
        String newStatus = "REALIZOVANO";
        when(prenosSredstavaRepository.findById(String.valueOf(idPrenosaSredstava))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transakcijaServis.promeniStatusPrenosaSredstava(String.valueOf(idPrenosaSredstava), newStatus, System.currentTimeMillis()));
    }

    /*
    @Test
    public void givenInvalidId_whenIzracunajRezervisanaSredstva_thenReturnNull() {
        Long idRacuna = -1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("PravniRacun");
        when(racunServis.nadjiAktivanPravniRacunPoID(idRacuna)).thenReturn(null);

        BigDecimal result = transakcijaServis.izracunajRezervisanaSredstva(idRacuna);

        assertNull(result);
    }

     */

    @Test
    public void givenInvalidId_whenVratiSredstva_thenReturnNull() {
        Long idRacuna = -1L;
        when(racunServis.nadjiVrstuRacuna(idRacuna)).thenReturn("PravniRacun");
        when(racunServis.nadjiAktivanPravniRacunPoID(idRacuna)).thenReturn(null);

        BigDecimal result = transakcijaServis.vratiSredstva(idRacuna);

        assertNull(result);
    }
}
