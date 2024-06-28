package rs.edu.raf.controller.racun;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.model.dto.racun.*;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.Firma;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;
import rs.edu.raf.service.racun.RacunServis;

import java.util.List;

@RestController
@RequestMapping("/racuni")
@Tag(name = "Racuni", description = "Operacije nad racunima")
@AllArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class RacunController {

    private RacunServis racunServis;


    @ApiOperation(value = "Dodaj devizni racun")
    @PostMapping("/dodajDevizni")
    public ResponseEntity<DevizniRacun> dodajDevizniRacun(@RequestBody NoviDevizniRacunDTO noviDevizniRacunDTO){
        return new ResponseEntity<>(racunServis.kreirajDevizniRacun(noviDevizniRacunDTO), HttpStatus.OK);
    }
    @ApiOperation(value = "Dodaj pravni racun")
    @PostMapping("/dodajPravni")
    public ResponseEntity<PravniRacun> dodajPravniRacun(@RequestBody NoviPravniRacunDTO noviPravniRacunDTO){
        return new ResponseEntity<>(racunServis.kreirajPravniRacun(noviPravniRacunDTO), HttpStatus.OK);
    }
    @ApiOperation(value = "Dodaj tekuci racun")
    @PostMapping("/dodajTekuci")
    public ResponseEntity<TekuciRacun> dodajTekuciRacun( @RequestBody NoviTekuciRacunDTO noviTekuciRacunDTO){
        return new ResponseEntity<>(racunServis.kreirajTekuciRacun(noviTekuciRacunDTO), HttpStatus.OK);
    }
    @ApiOperation(value = "Izlistaj racune korisnika")
    @GetMapping("/nadjiRacuneKorisnika/{idKorisnik}")
    public ResponseEntity<List<RacunDTO>> izlistajRacuneKorisnika(@PathVariable("idKorisnik") Long idKorisnik, @RequestHeader("Authorization") String token){
        return new ResponseEntity<>(racunServis.izlistavanjeRacunaJednogKorisnika(idKorisnik, token), HttpStatus.OK);
    }
    @ApiOperation(value = "Nadji racun")
    @GetMapping("/nadjiRacun/{id}")
    public ResponseEntity<RacunDTO> nadjiRacunPoId(@PathVariable("id") Long id){
        return new ResponseEntity<>(racunServis.nadjiAktivanRacunPoID(id), HttpStatus.OK);
    }
    @ApiOperation(value = "Nadji devizni racun")
    @GetMapping("/nadjiDevizniRacun/{id}")
    public ResponseEntity<DevizniRacun> nadjiDevizniRacunPoId(@PathVariable("id") Long id){
        return new ResponseEntity<>(racunServis.nadjiAktivanDevizniRacunPoID(id), HttpStatus.OK);
    }
    @ApiOperation(value = "Nadji pravni racun")
    @GetMapping("/nadjiPravniRacun/{id}")
    public ResponseEntity<PravniRacun> nadjiPravniRacunPoId(@PathVariable("id") Long id){
        return new ResponseEntity<>(racunServis.nadjiAktivanPravniRacunPoID(id), HttpStatus.OK);
    }
    @ApiOperation(value = "Nadji tekuci racun")
    @GetMapping("/nadjiTekuciRacun/{id}")
    public ResponseEntity<TekuciRacun> nadjiTekuciRacunPoId(@PathVariable("id") Long id){
        return new ResponseEntity<>(racunServis.nadjiAktivanTekuciRacunPoID(id), HttpStatus.OK);
    }
    @ApiOperation(value = "Nadji racun po broju racuna")
    @GetMapping("/nadjiRacunPoBroju/{brojRacuna}")
    public ResponseEntity<RacunDTO> nadjiRacunPoBroju(@PathVariable("brojRacuna") Long brojRacuna){
        return new ResponseEntity<>(racunServis.nadjiAktivanRacunPoBrojuRacuna(brojRacuna), HttpStatus.OK);
    }

    @ApiOperation(value = "Nadji racun po broju racuna")
    @PutMapping("/deleteRacunPoBroju/{brojRacuna}")
    public ResponseEntity<Boolean> obrisiRacun(@PathVariable("brojRacuna") Long brojRacuna) {
        return new ResponseEntity<>(racunServis.deaktiviraj(brojRacuna),HttpStatus.OK);
    }

    @ApiOperation(value = "Nadji devizni racun po broju racuna")
    @GetMapping("/nadjiDevizniRacunPoBroju/{brojRacuna}")
    public ResponseEntity<DevizniRacun> nadjiDevizniRacunPoBroju(@PathVariable("brojRacuna") Long brojRacuna){
        return new ResponseEntity<>(racunServis.nadjiAktivanDevizniRacunPoBrojuRacuna(brojRacuna), HttpStatus.OK);
    }
    @ApiOperation(value = "Nadji pravni racun po broju racuna")
    @GetMapping("/nadjiPravniRacunPoBroju/{brojRacuna}")
    public ResponseEntity<PravniRacun> nadjiPravniRacunPoBroju(@PathVariable("brojRacuna") Long brojRacuna){
        return new ResponseEntity<>(racunServis.nadjiAktivanPravniRacunPoBrojuRacuna(brojRacuna), HttpStatus.OK);
    }
    @ApiOperation(value = "Nadji tekuci racun po broju racuna")
    @GetMapping("/nadjiTekuciRacunPoBroju/{brojRacuna}")
    public ResponseEntity<TekuciRacun> nadjiTekuciRacunPoBroju(@PathVariable("brojRacuna") Long brojRacuna){
        return new ResponseEntity<>(racunServis.nadjiAktivanTekuciRacunPoBrojuRacuna(brojRacuna), HttpStatus.OK);
    }
    @ApiOperation(value = "Izlistaj sve firme")
    @GetMapping("/izlistajSveFirme")
    public ResponseEntity<List<FirmaDTO>> izlistajSveFirme(@RequestHeader("Authorization") String authorization){
        return new ResponseEntity<>(racunServis.izlistajSveFirme(), HttpStatus.OK);
    }

    @ApiOperation(value = "Dohvati firmu preko id")
    @GetMapping("/firma/{id}")
    public ResponseEntity<FirmaDTO> izlistajSveFirme(@PathVariable("id") Long id){
        return new ResponseEntity<>(racunServis.dohvatiFirmu(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Kreiraj novu firmu")
    @PostMapping("/kreirajFirmu")
    public ResponseEntity<Firma> dodajDevizniRacun( @RequestBody NovaFirmaDTO novaFirmaDTO){
        return new ResponseEntity<>(racunServis.kreirajFirmu(novaFirmaDTO), HttpStatus.OK);
    }

    @PostMapping("/atm")
    @ApiOperation(value = "Atm")
    public ResponseEntity<?> atm(@RequestBody AtmDto atmDto) {
        return new ResponseEntity<>(racunServis.bankomat(atmDto.getBrojRacuna(),atmDto.getStanje()),HttpStatus.OK);
    }






}
