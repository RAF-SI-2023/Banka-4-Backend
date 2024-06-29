package rs.edu.raf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.dto.IzmenaRadnikaDTO;
import rs.edu.raf.dto.NoviRadnikDTO;
import rs.edu.raf.dto.RadnikDTO;
import rs.edu.raf.model.Radnik;
import rs.edu.raf.repository.RadnikRepository;
import rs.edu.raf.servis.KorisnikServis;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/radnik")
@AllArgsConstructor
@SecurityRequirement(name="jwt")
@CrossOrigin(origins = "*")
public class RadnikController {
    private KorisnikServis korisnikServis;
    private RadnikRepository radnikRepository;

    @PostMapping
    @Operation(description = "Dodaj novog radnika")
    public ResponseEntity<RadnikDTO> dodajRadnika(@RequestBody @Valid @Parameter(description = "Podaci o radniku") NoviRadnikDTO noviRadnikDTO) {
        System.out.println(noviRadnikDTO);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            id = ((Radnik) authentication.getPrincipal()).getFirmaId();
        }
        return new ResponseEntity<>(korisnikServis.kreirajNovogRadnika(noviRadnikDTO,id), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(description = "Izmeni postojeceg radnika")
    public ResponseEntity<RadnikDTO> izmeniRadnika(@RequestBody @Valid @Parameter(description = "Novi podaci o radniku") IzmenaRadnikaDTO izmenaRadnikaDTO) {
        return new ResponseEntity<>(korisnikServis.izmeniRadnika(izmenaRadnikaDTO),HttpStatus.OK);
    }

    @GetMapping
    @Operation(description = "Svi radnici")
    public ResponseEntity<List<RadnikDTO>> sviAktivniRadnici() {
        return new ResponseEntity<>(korisnikServis.izlistajSveAktivneRadnike(),HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    @Operation(description = "Podaci o radniku koji ima email")
    public ResponseEntity<RadnikDTO> nadjiRadnikaPoEmail(@PathVariable("email") @Parameter(description = "email radnika") String email) {
        return new ResponseEntity<>(korisnikServis.nadjiAktivnogRadnikaPoEmail(email),HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @Operation(description = "Podaci o radniku koji ima email")
    public ResponseEntity<RadnikDTO> nadjiRadnikaPoId(@PathVariable("id") @Parameter(description = "id radnika") Long id) {
        return new ResponseEntity<>(korisnikServis.findWorkerById(id),HttpStatus.OK);
    }

    @PutMapping("/reset-limit/{id}")
    @Operation(description = "Resetovanje limita za zaposlenog")
    public ResponseEntity<RadnikDTO> resetLimit(@PathVariable("id") Long radnikId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = null;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            Radnik radnik = radnikRepository
                    .findByEmailAndAktivanIsTrue(((UserDetails) authentication.getPrincipal()).getUsername()).orElseThrow();
            id = radnik.getFirmaId();
        }
        return new ResponseEntity<>(korisnikServis.resetLimit(radnikId,id),HttpStatus.OK);
    }

    @PutMapping("/update-daily-spent/{id}/{price}")
    public void update(@PathVariable("id") Long id, @PathVariable("price")BigDecimal price) {
        korisnikServis.updateDailySpent(id,price);
    }

    @PutMapping("/profit/{id}/{price}")
    public void updateProfit(@PathVariable("id") Long id, @PathVariable("price")BigDecimal price) {
        korisnikServis.updateProfit(id,price);
    }

}

