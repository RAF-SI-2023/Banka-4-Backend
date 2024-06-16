package rs.edu.raf.controller;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.edu.raf.controller.racun.RacunController;
import rs.edu.raf.model.dto.racun.NoviDevizniRacunDTO;
import rs.edu.raf.model.dto.racun.NoviPravniRacunDTO;
import rs.edu.raf.model.dto.racun.NoviTekuciRacunDTO;
import rs.edu.raf.model.dto.racun.RacunDTO;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.service.racun.RacunServis;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RacunControllerTest {
    @Mock
    private RacunServis racunServis;

    @InjectMocks
    private RacunController racunController;

    @Test
    public void testDodajDevizniRacun() {

        NoviDevizniRacunDTO noviDevizniRacunDTO = new NoviDevizniRacunDTO();
        DevizniRacun devizniRacun = new DevizniRacun();
        when(racunServis.kreirajDevizniRacun(any(NoviDevizniRacunDTO.class))).thenReturn(devizniRacun);


        ResponseEntity<DevizniRacun> response = racunController.dodajDevizniRacun(noviDevizniRacunDTO);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(devizniRacun, response.getBody());
    }

    @Test
    public void testDodajPravniRacun() {

        NoviPravniRacunDTO noviPravniRacunDTO = new NoviPravniRacunDTO();
        PravniRacun pravniRacun = new PravniRacun();
        when(racunServis.kreirajPravniRacun(any(NoviPravniRacunDTO.class))).thenReturn(pravniRacun);


        ResponseEntity<PravniRacun> response = racunController.dodajPravniRacun(noviPravniRacunDTO);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pravniRacun, response.getBody());
    }

    @Test
    public void testDodajTekuciRacun() {

        NoviTekuciRacunDTO noviTekuciRacunDTO = new NoviTekuciRacunDTO();
        TekuciRacun tekuciRacun = new TekuciRacun();
        when(racunServis.kreirajTekuciRacun(any(NoviTekuciRacunDTO.class))).thenReturn(tekuciRacun);


        ResponseEntity<TekuciRacun> response = racunController.dodajTekuciRacun(noviTekuciRacunDTO);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tekuciRacun, response.getBody());
    }
    /*
    @Test
    public void testIzlistajRacuneKorisnika() {

        Long idKorisnik = 123L;
        List<RacunDTO> racuni = new ArrayList<>();

        ///when(racunServis.izlistavanjeRacunaJednogKorisnika(anyLong())).thenReturn(racuni);


        ResponseEntity<List<RacunDTO>> response = racunController.izlistajRacuneKorisnika(idKorisnik);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(racuni, response.getBody());
    }
    */
    @Test
    public void testNadjiRacunPoId() {

        Long idRacuna = 456L;
        RacunDTO racunDTO = new RacunDTO();

        when(racunServis.nadjiAktivanRacunPoID(anyLong())).thenReturn(racunDTO);


        ResponseEntity<RacunDTO> response = racunController.nadjiRacunPoId(idRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(racunDTO, response.getBody());
    }

    @Test
    public void testNadjiDevizniRacunPoId() {

        Long idRacuna = 456L;
        DevizniRacun devizniRacun = new DevizniRacun();

        when(racunServis.nadjiAktivanDevizniRacunPoID(anyLong())).thenReturn(devizniRacun);


        ResponseEntity<DevizniRacun> response = racunController.nadjiDevizniRacunPoId(idRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(devizniRacun, response.getBody());
    }

    @Test
    public void testNadjiPravniRacunPoId() {

        Long idRacuna = 456L;
        PravniRacun pravniRacun = new PravniRacun();

        when(racunServis.nadjiAktivanPravniRacunPoID(anyLong())).thenReturn(pravniRacun);


        ResponseEntity<PravniRacun> response = racunController.nadjiPravniRacunPoId(idRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pravniRacun, response.getBody());
    }

    @Test
    public void testNadjiTekuciRacunPoId() {

        Long idRacuna = 456L;
        TekuciRacun tekuciRacun = new TekuciRacun();

        when(racunServis.nadjiAktivanTekuciRacunPoID(anyLong())).thenReturn(tekuciRacun);


        ResponseEntity<TekuciRacun> response = racunController.nadjiTekuciRacunPoId(idRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tekuciRacun, response.getBody());
    }

    @Test
    public void testNadjiRacunPoBroju() {

        Long brojRacuna = 123L;
        RacunDTO racunDTO = new RacunDTO();

        when(racunServis.nadjiAktivanRacunPoBrojuRacuna(anyLong())).thenReturn(racunDTO);


        ResponseEntity<RacunDTO> response = racunController.nadjiRacunPoBroju(brojRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(racunDTO, response.getBody());
    }

    @Test
    public void testNadjiDevizniRacunPoBroju() {

        Long brojRacuna = 123L;
        DevizniRacun devizniRacun = new DevizniRacun();

        when(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(anyLong())).thenReturn(devizniRacun);


        ResponseEntity<DevizniRacun> response = racunController.nadjiDevizniRacunPoBroju(brojRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(devizniRacun, response.getBody());
    }

    @Test
    public void testNadjiPravniRacunPoBroju() {

        Long brojRacuna = 123L;
        PravniRacun pravniRacun = new PravniRacun();

        when(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(anyLong())).thenReturn(pravniRacun);


        ResponseEntity<PravniRacun> response = racunController.nadjiPravniRacunPoBroju(brojRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pravniRacun, response.getBody());
    }

    @Test
    public void testNadjiTekuciRacunPoBroju() {

        Long brojRacuna = 123L;
        TekuciRacun tekuciRacun = new TekuciRacun();

        when(racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(anyLong())).thenReturn(tekuciRacun);


        ResponseEntity<TekuciRacun> response = racunController.nadjiTekuciRacunPoBroju(brojRacuna);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tekuciRacun, response.getBody());
    }
}
