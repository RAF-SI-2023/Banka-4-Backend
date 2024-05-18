package rs.edu.raf.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.dto.OmiljeniKorisnikDTO;
import rs.edu.raf.servis.OmiljeniKorisnikServis;

import java.util.List;

@RestController
@RequestMapping("/omiljeni-korisnik")
@Tag(name = "OmiljeniKorisnik", description = "Operacije nad Omiljenim Korisnicima")
@AllArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class OmiljeniKorisnikController {

    private OmiljeniKorisnikServis omiljeniKorisnikServis;


    @ApiOperation(value = "Dodaj Omiljenog Korisnika")
    @PostMapping("")
    public ResponseEntity<OmiljeniKorisnikDTO> dodajOmiljenogKorisnika(@RequestBody OmiljeniKorisnikDTO omiljeniKorisnikDTO){
        return new ResponseEntity<>(omiljeniKorisnikServis.add(omiljeniKorisnikDTO), HttpStatus.OK);
    }

    @ApiOperation(value = "Dohvati Omiljenog Korisnika po id")
    @GetMapping("/{id}")
    public ResponseEntity<List<OmiljeniKorisnikDTO>> dohvatiOmiljenogKorisnika(@PathVariable("id") Long id){
        return new ResponseEntity<>(omiljeniKorisnikServis.findByIdKorisnika(id), HttpStatus.OK);
    }

    @ApiOperation(value = "Obrisi Omiljenog Korisnika po id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> obrisiOmiljenogKorisnika(@PathVariable("id") Long id){
        omiljeniKorisnikServis.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
