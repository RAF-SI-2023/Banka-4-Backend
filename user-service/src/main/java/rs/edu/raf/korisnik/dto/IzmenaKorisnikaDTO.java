package rs.edu.raf.korisnik.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class IzmenaKorisnikaDTO {
    
    private Long id;

    @Pattern(regexp = "^[a-zA-Z]+$\n", message = "Samo jedno ili više slova su dozvoljena!")
    private String prezime;

    @Pattern(regexp = "^[M|F]$", message = "Pol može biti M ili F!")
    private String pol;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email adresa mora biti validna!")
    private String email;

    @Pattern(regexp = "^(\\+381|0)6[0-6]\\d{6,7}$\n", message = "Broj telefona mora biti formata +381/0 6 0-6 praćen sa 6 ili 7 cifara!")
    private String brojTelefona;

    @NotNull
    private String adresa;
    
    private String password;
    
    private String povezaniRacuni;

    private boolean aktivan;
}
