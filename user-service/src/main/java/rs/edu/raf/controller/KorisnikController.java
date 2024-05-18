package rs.edu.raf.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.dto.*;

import rs.edu.raf.security.JwtUtil;
import rs.edu.raf.security.Permisije;
import rs.edu.raf.servis.KorisnikServis;
import rs.edu.raf.servis.KodServis;
import rs.edu.raf.servis.MailServis;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@RestController
@RequestMapping("/korisnik")
@AllArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class KorisnikController {
    private KorisnikServis korisnikServis;
    private KodServis kodServis;
    private MailServis mailServis;
    private JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(description = "Logovanje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uspesno logovanje"),
            @ApiResponse(responseCode = "401", description = "Losi kredencijali")
    })
    public ResponseEntity<String> login(@RequestBody @Valid @Parameter(description = "Kredencijali za logovanje") LoginDto loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(jwtUtil.generateToken(loginRequest.getUsername()));
    }


    @PostMapping("/add")
    @Operation(description = "Dodaj novog korisnika")
    public ResponseEntity<KorisnikDTO> dodajKorisnika(@RequestBody @Valid @Parameter(description = "Podaci o korisniku") NoviKorisnikDTO noviKorisnikDTO) {
        return new ResponseEntity<>(korisnikServis.kreirajNovogKorisnika(noviKorisnikDTO), HttpStatus.OK);
    }

    @PutMapping
    @Operation(description = "Izmeni postojeceg korisnika")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uspesna promena korisnika"),
            @ApiResponse(responseCode = "403", description = "Mozes samo sebi da menjas podatke")
    })

    public ResponseEntity<KorisnikDTO> izmeniKorisnika(@RequestBody @Valid @Parameter(description = "Novi podaci o korisniku") IzmenaKorisnikaDTO izmenaKorisnikaDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        Long permission = 0L;
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
            List<? extends GrantedAuthority> authorityList = new ArrayList<>(((UserDetails) authentication.getPrincipal()).getAuthorities());
            permission = Long.parseLong(authorityList.get(0).getAuthority());
        }

        if (username.equals(izmenaKorisnikaDTO.getEmail()) || (permission & Permisije.edit_users) == Permisije.edit_users)
            return new ResponseEntity<>(korisnikServis.izmeniKorisnika(izmenaKorisnikaDTO), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping
    @Operation(description = "Svi korisnici")
    public ResponseEntity<List<KorisnikDTO>> sviKorisnici() {
        return new ResponseEntity<>(korisnikServis.izlistajSveAktivneKorisnike(), HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    @Operation(description = "Nadji korisnika koji ima odredjeni email")
    public ResponseEntity<KorisnikDTO> nadjiKorisnikaPoEmail(@PathVariable("email") @Parameter(description = "Email trazenog korisnika") String email) {
        return new ResponseEntity<>(korisnikServis.nadjiAktivnogKorisnikaPoEmail(email), HttpStatus.OK);
    }

    @GetMapping("/jmbg/{jmbg}")
    @Operation(description = "Nadji korisnika koji ima odredjeni jmbg")
    public ResponseEntity<KorisnikDTO> nadjiKorisnikaPoJmbg(@PathVariable("jmbg") @Parameter(description = "JMBG korisnika") String jmbg) {
        return new ResponseEntity<>(korisnikServis.nadjiAktivnogKorisnikaPoJMBG(jmbg), HttpStatus.OK);
    }

    @GetMapping("/telefon/{telefon}")
    @Operation(description = "Nadji korisnika koji ima odredjeni broj telefona")
    public ResponseEntity<KorisnikDTO> nadjiKorisnikaPoBrojuTelefona(@PathVariable("telefon") @Parameter(description = "Broj telefona korisnika") String telefon) {
        return new ResponseEntity<>(korisnikServis.nadjiAktivnogKorisnikaPoBrojuTelefona(telefon), HttpStatus.OK);
    }


    @PostMapping("/generate-login")
    @Operation(description = "Generisanje koda pri prvom logovanju korisnika")
    public ResponseEntity<String> generisiKod(@RequestBody @Valid @Parameter(description = "Podaci o korisniku") DodavanjeSifreDTO dodavanjeSifreDTO) {
        String kod = UUID.randomUUID().toString();

        KorisnikDTO korisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoEmail(dodavanjeSifreDTO.getEmail());

        mailServis.posaljiMailZaRegistraciju(korisnikDTO, kod);
        kodServis.dodajKod(dodavanjeSifreDTO.getEmail(), kod, new Date(System.currentTimeMillis() + 1000 * 60 * 15).getTime(), false);
        return new ResponseEntity<>("Kod je poslat", HttpStatus.OK);
    }

    @PostMapping("/generate-reset")
    @Operation(description = "Slanje koda za resetovanje sifre na email korisnika")
    public ResponseEntity<String> generisiKodResetovanje(@RequestBody @Valid @Parameter(description = "Podaci o korisniku") DodavanjeSifreDTO dodavanjeSifreDTO) {
        String kod = UUID.randomUUID().toString();
        KorisnikDTO korisnikDTO = korisnikServis.nadjiAktivnogKorisnikaPoEmail(dodavanjeSifreDTO.getEmail());
        mailServis.posaljiMailZaPromenuLozinke(korisnikDTO, kod);
        kodServis.dodajKod(dodavanjeSifreDTO.getEmail(), kod, new Date(System.currentTimeMillis() + 1000 * 60 * 15).getTime(), true);
        return new ResponseEntity<>("Kod je poslat", HttpStatus.OK);
    }

    @PostMapping("/verifikacija")
    @Operation(description = "Verifikacija koda i postavljanje sifre korisnika")
    public ResponseEntity<Boolean> registruj(@RequestBody @Valid @Parameter(description = "Podaci o korisniku i sfri") RegistrujKorisnikDTO registrujKorisnikDTO) {
        return new ResponseEntity<>(korisnikServis.registrujNovogKorisnika(registrujKorisnikDTO), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    @Operation(description = "Postavljanje nove sifre")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid @Parameter(description = "Nova sifra i postojeci podaci") IzmenaSifreUzKodDto izmenaSifreUzKodDto) {
        return new ResponseEntity<>(korisnikServis.promeniSifruKorisnikaUzKod(izmenaSifreUzKodDto), HttpStatus.OK);
    }

    @PostMapping("/change-password")
    @Operation(description = "Promena sifre")
    public ResponseEntity<KorisnikDTO> changePassword(@RequestBody @Parameter(description = "Stara i nova sifra korisnika") IzmenaSifreDto izmenaSifreDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }

        return new ResponseEntity<>(korisnikServis.promeniSifruKorisnika(username, izmenaSifreDto), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    @Operation(description = "Nadji korisnika po id")
    public ResponseEntity<KorisnikDTO> nadjiKorisnikaPoId(@PathVariable("id") @Parameter(description = "Broj id korisnika") Long id) {
        return new ResponseEntity<>(korisnikServis.findUserById(id), HttpStatus.OK);
    }

    @GetMapping("/addAccount/{id}/{accountNumber}")
    public ResponseEntity<Boolean> addAccountToUser(@PathVariable("id") Long id, @PathVariable("accountNumber") Long accountNumber) {
        return new ResponseEntity<Boolean>(korisnikServis.addAccountToUser(id, accountNumber), HttpStatus.OK);
    }
}
