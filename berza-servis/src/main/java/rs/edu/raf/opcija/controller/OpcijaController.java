package rs.edu.raf.opcija.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.opcija.dto.NovaOpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaKorisnikaDto;
import rs.edu.raf.opcija.model.KorisnikoveKupljeneOpcije;
import rs.edu.raf.opcija.model.OpcijaStanje;
import rs.edu.raf.opcija.servis.OpcijaServis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/opcija")
@CrossOrigin(origins = "*")
public class OpcijaController {

    @Autowired
    private OpcijaServis opcijaServis;


    /*@ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "")
    })*/

    @PostMapping("/kreiraj-opciju")
    @Operation(description = "Kreiraj opciju")      //moze @Valid u kontroleru ili u servisu ekplicitno validator
    public ResponseEntity<OpcijaDto> kreirajOpciju(@Valid @RequestBody NovaOpcijaDto novaOpcijaDto) {
       return new ResponseEntity<>(opcijaServis.save(novaOpcijaDto),HttpStatus.OK);

    }

    @PostMapping("/kreiraj-opciju-za-korisnika")
    @Operation(description = "Kreiraj opciju za korisnika")
    public ResponseEntity<Void> kreirajOpcijuZaKorisnika(@Valid @RequestBody OpcijaKorisnikaDto opcijaKorisnikaDto){
        boolean success = opcijaServis.novaOpcijaKorisnika(opcijaKorisnikaDto);
        if(success){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sve-opcije")
    @Operation(description = "uzmi sve opcije")
    public ResponseEntity<List<OpcijaDto>> findAll() throws InterruptedException {
        return new ResponseEntity<>(opcijaServis.findAll(),HttpStatus.OK);

    }

    @GetMapping("/sve-opcije-korisnika/{id}")
    @Operation(description = "uzmi sve opcije korisnika")
    public ResponseEntity<List<OpcijaKorisnikaDto>> findAllForKorisnik(@PathVariable("id") Long opcijaId){
        return new ResponseEntity<>(opcijaServis.findAllForUser(opcijaId),HttpStatus.OK);
    }

    @GetMapping("/opcija-po-id/{opcijaId}")
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
            LocalDateTime.ofInstant(Instant.ofEpochSecond(datumIsteka), ZoneOffset.systemDefault());
        }
        return new ResponseEntity<>(this.opcijaServis.findByStockAndDateAndStrike(ticker,localDate,strikePrice), HttpStatus.OK);
    }
    /////////////////////////////////////////////////////////////////////////

    @GetMapping("/opcije/{ticker}")
    @Operation(description = "Dobavi sve put i call opcije za zadati ticker")
    public ResponseEntity<Map<String, List<OpcijaDto>>> getPutsAndCallsByTicker(@PathVariable String ticker) {
        try {
            Map<String, List<OpcijaDto>> putsAndCalls = opcijaServis.findPutsAndCallsByStockTicker(ticker);
            return ResponseEntity.ok(putsAndCalls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/opcije/{ticker}/po-datumu-isteka/{datumIsteka}")
    public ResponseEntity<Map<String, Object>> getPutsAndCallsByTickerAndExpirationDate(@PathVariable String ticker,
                                                                                        @PathVariable("datumIsteka") String datumIstekaStr) {
        Date datumIsteka;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            datumIsteka = sdf.parse(datumIstekaStr);
        } catch (ParseException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        long startOfDay = getStartOfDay(datumIsteka).getTime();
        long endOfDay = getEndOfDay(datumIsteka).getTime();

        Map<String, Object> result = new HashMap<>();
        result.put("pocetakDana", startOfDay);
        result.put("krajDana", endOfDay);

        Map<String, List<OpcijaDto>> opcije = opcijaServis.findPutsAndCallsByStockTickerAndExpirationDate(ticker, getStartOfDay(datumIsteka), getEndOfDay(datumIsteka));
        if (opcije.get("calls").isEmpty() && opcije.get("puts").isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        result.put("opcije", opcije);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    @GetMapping("/klasifikuj-opcije/{ticker}")
    @Operation(description = "Klasifikuj opcije kao In-The-Money ili Out-Of-The-Money")
    public ResponseEntity<Map<String, List<OpcijaDto>>> klasifikujOpcije(@PathVariable String ticker) {
        try {
            Map<String, List<OpcijaDto>> klasifikovaneOpcije = opcijaServis.classifyOptions(ticker);
            if (klasifikovaneOpcije != null && (!klasifikovaneOpcije.get("ITM").isEmpty() || !klasifikovaneOpcije.get("OTM").isEmpty())) {
                return ResponseEntity.ok(klasifikovaneOpcije);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            // Logovanje greške
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }







}
