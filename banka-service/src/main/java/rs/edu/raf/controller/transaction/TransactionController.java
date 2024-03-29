package rs.edu.raf.controller.transaction;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.edu.raf.model.dto.transaction.NovaUplataDTO;
import rs.edu.raf.model.dto.transaction.NoviPrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.PrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.UplataDTO;
import rs.edu.raf.model.entities.transaction.PrenosSredstava;
import rs.edu.raf.model.entities.transaction.Uplata;
import rs.edu.raf.service.transaction.TransakcijaServis;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@CrossOrigin(origins = "*")
@SecurityRequirement(name = "jwt")
public class TransactionController {

    public TransakcijaServis transakcijaServis;

    //AUTOMATSKI CONSTRUCTOR INJECT IMPLEMENTACIJE
    //AKO IMA VISE IMPLEMENTACIJA,MORA SE NAVESTI KONKRETNA PREKO QUALIFIER
    @Autowired
    public TransactionController(TransakcijaServis transakcijaServis) {
        this.transakcijaServis = transakcijaServis;
    }

    /////////////////////////////////////////////////////////////////////////////////////////
    @Tag(name = "TRANSAKCIJE", description = "Transakcija API")
    @Operation(summary ="Kreiranje nove uplate",description = "proverena ispravnost uplata transakcije")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "uspesna provera ispravnosti"),
    })
    @PostMapping(value = "/nova-uplata",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Uplata> novaUplata(@RequestBody NovaUplataDTO novaUplataDTO){
        return new ResponseEntity<>(transakcijaServis.sacuvajUplatu(novaUplataDTO),HttpStatus.OK);
    }
    @Tag(name = "TRANSAKCIJE", description = "Transakcija API")
    @Operation(summary ="PROVERA ISPRAVNOSTI PRENOS SREDSTAVA TRANSAKCIJE SA JEDNOKRATNOM LOZINKOM",description = "proverena ispravnost prenos sredstava transakcije")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "uspesna provera ispravnosti"),
    })
    @PostMapping(value = "/novi-prenos",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrenosSredstava> proveraIspravnostiPrenosSredstavaTransaction(@RequestBody NoviPrenosSredstavaDTO noviPrenosSredstavaDTO){
        return new ResponseEntity<>(transakcijaServis.sacuvajPrenosSredstava(noviPrenosSredstavaDTO),HttpStatus.OK);
    }
    /////////////////////////////////////////////////////////////////////////////////////////


    @Tag(name = "TRANSAKCIJE", description = "Transakcija API")
    @Operation(summary ="DOHVATANJE SVIH UPLATA TRANSAKCIJA PO BROJU RACUNA",description = "prosledjuje se u path param id racuna cije transakcije trebaju da se vrate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "uspesno vracene transakcije racuna"),
    })
    @GetMapping(value = "/getAllUplateByBrojRacuna/{brojRacuna}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UplataDTO>> getAllUplateByBrojRacuna(@PathVariable("brojRacuna") Long billId) {

        List<UplataDTO> uplateKaILIOdKorisnika = new ArrayList<>();

        List<UplataDTO> uplataDTO1 = transakcijaServis.dobaciUplatuSretstavaDTOPoBrojuPrimaoca(billId);
        List<UplataDTO> uplataDTO2 = transakcijaServis.dobaciUplatuSretstavaDTOPoBrojuPosiljaoca(billId);

        if(uplataDTO1 != null && uplataDTO2 != null) {
          //SAMO 2 UPLATE JER METODE SERVISA NE VRACAJU LISTU
            uplateKaILIOdKorisnika.addAll(uplataDTO1);
            uplateKaILIOdKorisnika.addAll(uplataDTO2);

            return new ResponseEntity<>(uplateKaILIOdKorisnika,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    @Tag(name = "TRANSAKCIJE", description = "Transakcija API")
    @Operation(summary ="DOHVATANJE SVIH PRENOS SREDSTAVA TRANSAKCIJA PO BROJU RACUNA",description = "prosledjuje se u path param id racuna cije transakcije trebaju da se vrate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "uspesno vracene transakcije racuna"),
    })

    @GetMapping(value = "/getAllPrenosSredstavaByBrojRacuna/{brojRacuna}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PrenosSredstavaDTO>> getAllPrenosSredstavaByBrojRacuna(@PathVariable("brojRacuna") Long billId) {

        List<PrenosSredstavaDTO> prenosiSredstavaKaILIOdKorisnika = new ArrayList<>();

        List<PrenosSredstavaDTO> prenosSredstavaDTO1 = transakcijaServis.dobaviPrenosSretstavaDTOPoBrojuPrimaoca(billId);
        List<PrenosSredstavaDTO>  prenosSredstavaDTO2 = transakcijaServis.dobaviPrenosSretstavaDTOPoBrojuPosiljaoca(billId);

        if(prenosSredstavaDTO1 != null && prenosSredstavaDTO2 != null) {
            //SAMO 2 PRENOSTA SREDSTAVA JER METODE SERVISA NE VRACAJU LISTU
            prenosiSredstavaKaILIOdKorisnika.addAll(prenosSredstavaDTO1);
            prenosiSredstavaKaILIOdKorisnika.addAll(prenosSredstavaDTO2);

            return new ResponseEntity<>(prenosiSredstavaKaILIOdKorisnika, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    @Tag(name = "TRANSAKCIJE", description = "Transakcija API")
    @Operation(summary ="DOHVATANJE UPLATE PREKO ID UPLATE",description = "prosledjuje se u path id uplate koja treba da se vrati")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "uspesno vracena uplata"),
    })
    @GetMapping(value = "/getUplataTransactionByTransactionId/{uplataId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UplataDTO> getUplataTransactionById(@PathVariable("uplataId") String transactionId) {

        UplataDTO uplataDTO = transakcijaServis.dobaciUplatuSretstavaDTOPoID(transactionId);
        if(uplataDTO != null)
            return new ResponseEntity<>(uplataDTO, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

    @Tag(name = "TRANSAKCIJE", description = "Transakcija API")
    @Operation(summary ="DOHVATANJE PRENOSA SREDSTAVA PREKO ID PRENOSA SREDSTAVA",description = "prosledjuje se u path id prenosa sredstava koji treba da se vrati")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "uspesno vracen prenos sredstava"),
    })
    @GetMapping(value = "/getPrenosSredstavaTransactionByTransactionId/{prenosSredstavaId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PrenosSredstavaDTO> getPrenosSredstavaTransactionById(@PathVariable("prenosSredstavaId") String transactionId) {

        PrenosSredstavaDTO prenosSredstavaDTO = transakcijaServis.dobaviPrenosSretstavaDTOPoID(transactionId);

        if(prenosSredstavaDTO != null)
            return new ResponseEntity<>(prenosSredstavaDTO, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

}
