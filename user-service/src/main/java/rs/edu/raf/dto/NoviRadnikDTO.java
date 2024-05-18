package rs.edu.raf.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NoviRadnikDTO {

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Samo jedno ili više slova su dozvoljena!")
    private String ime;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Samo jedno ili više slova su dozvoljena!")
    private String prezime;


    private String jmbg;

    private Long datumRodjenja;

    @Pattern(regexp = "^[M|F]$", message = "Pol može biti M ili F!")
    private String pol;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email adresa mora biti validna!")
    private String email;

    @Pattern(regexp = "^(\\+381|0)6\\d{7,8}$", message = "Broj telefona mora biti formata +381/0 6 praćen sa 6 ili 7 cifara!")
    private String brojTelefona;

    private String adresa;

    @NotNull
    private String username;

    private String password;

    private String saltPassword;

    @NotNull
    private String pozicija;

    @NotNull
    private String departman;

    @PositiveOrZero
    private Long permisije;

    private boolean aktivan;

    @PositiveOrZero
    private BigDecimal dailyLimit;
    private boolean approvalFlag;
    private boolean supervisor;
}
