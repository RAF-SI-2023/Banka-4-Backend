package rs.edu.raf.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class KorisnikDTO {

    private Long id;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Samo jedno ili više slova su dozvoljena!")
    private String ime;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Samo jedno ili više slova su dozvoljena!")
    private String prezime;

    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])(0[1-9]|1[0-2])(\\d{2})(\\d{3})$", message = "Nepravilan JMBG!")
    private String jmbg;

    private Long datumRodjenja;

    @Pattern(regexp = "^[M|F]$", message = "Pol može biti M ili F!")
    private String pol;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email adresa mora biti validna!")
    private String email;

    @Pattern(regexp = "^(\\+381|0)6\\d{7,8}$", message = "Broj telefona mora biti formata +381/0 6 praćen sa 6 ili 7 cifara!")
    private String brojTelefona;

    @NotNull
    private String adresa;

    private String povezaniRacuni;

}
