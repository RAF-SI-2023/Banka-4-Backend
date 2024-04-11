package rs.edu.raf.opcija.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.opcija.dto.NovaOpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaDto;
import rs.edu.raf.opcija.model.KorisnikoveKupljeneOpcije;
import rs.edu.raf.opcija.model.OpcijaStanje;
import rs.edu.raf.opcija.servis.OpcijaServis;

import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/opcija")
public class OpcijaController {

    @Autowired
    private OpcijaServis opcijaServis;


    /*@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "")
    })*/

    @PostMapping("kreiraj-opciju")
    @Operation(description = "Kreiraj opciju")      //moze @Valid u kontroleru ili u servisu ekplicitno validator
    public ResponseEntity<OpcijaDto> kreirajOpciju(@Valid @RequestBody NovaOpcijaDto novaOpcijaDto) {
        return new ResponseEntity<>(opcijaServis.save(novaOpcijaDto),HttpStatus.OK);

    }

    @GetMapping("sve-opcije")
    @Operation(description = "uzmi sve opcije")
    public ResponseEntity<List<OpcijaDto>> findAll() throws InterruptedException {
        return new ResponseEntity<>(opcijaServis.findAll(),HttpStatus.OK);

    }

    @GetMapping("opcija-po-id/{opcijaId}")
    @Operation(description = "uzmi opciju po id")
    public ResponseEntity<OpcijaDto> findOne(@PathVariable("opcijaId") Long opcijaId) {
        OpcijaDto opcijaDto = opcijaServis.findById(opcijaId);
        if (opcijaDto != null) {
            return ResponseEntity.ok(opcijaDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/izvrsi-korisnikovu-opciju/{opcijaId}/{userId}")
    @Operation(description = "Izvrsi put ili call opciju\nVraca not found pri nedostupnom podatku")
    public ResponseEntity<KorisnikoveKupljeneOpcije> izvrsiKorisnikovuOpciju(@PathVariable("opcijaId") Long opcijaId, @PathVariable("userId") Long userId) {
        KorisnikoveKupljeneOpcije korisnikoveKupljeneOpcije = opcijaServis.izvrsiOpciju(opcijaId, userId);

        if (korisnikoveKupljeneOpcije != null) {
            return ResponseEntity.ok(korisnikoveKupljeneOpcije);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stanje-opcije/{opcijaId}")
    @Operation(description = "Proveri stanje opcije\nVraca not found pri nedostupnom podatku")
    public ResponseEntity<OpcijaStanje> stanjeOpcije(@PathVariable("opcijaId") Long opcijaId){
        OpcijaStanje stanje = opcijaServis.proveriStanjeOpcije(opcijaId);

        if (stanje != null) {
            return ResponseEntity.ok(stanje);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //query parametri
    @Operation(description = "Filtriraj opcije\nDostupni parametri:datumIsteka(u milisec),ticker,strikePrice\nParametri mogu biti prosledjeni u bilo kom redosledu i bilo koji se moze izostaviti")
    @GetMapping(value = "/filtriraj-opcije", produces = MediaType.APPLICATION_JSON_VALUE)
    //parametri se moraju zvati ovako ako su prosledjeni ali ne moraju biti svi prosledjeni onda ce biti null
    public ResponseEntity<List<OpcijaDto>> filtrirajOpcije(@RequestParam(name = "ticker",required = false) String ticker,
                                                           @RequestParam(name = "datumIsteka",required = false) Long datumIsteka,//u milisec
                                                           @RequestParam(name = "strikePrice",required = false) Double strikePrice
    ){
        LocalDateTime localDate = null;
        if(datumIsteka != null) {
            //localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(datumIsteka),ZoneId.systemDefault()); // Konvertovanje u LocalDate
            localDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(datumIsteka), ZoneOffset.systemDefault());
        }
        return new ResponseEntity<>(this.opcijaServis.findByStockAndDateAndStrike(ticker,localDate,strikePrice), HttpStatus.OK);
    }
    /////////////////////////////////////////////////////////////////////////



}
