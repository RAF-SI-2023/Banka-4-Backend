package rs.edu.raf.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class IzmenaRadnikaDTO {

    private Long id;

    @Pattern(regexp = "^[a-zA-Z]+$", message = "Samo jedno ili više slova su dozvoljena!")
    private String prezime;

    @Pattern(regexp = "^[M|F]$", message = "Pol može biti M ili F!")
    private String pol;

    @Pattern(regexp = "^(\\+381|0)6\\d{7,8}$", message = "Broj telefona mora biti formata +381/0 6 praćen sa 6 ili 7 cifara!")
    private String brojTelefona;

    private String adresa;

    private String password;

    private String pozicija;

    private String departman;

    @PositiveOrZero
    private Long permisije;

    private Boolean aktivan;
    private boolean approvalFlag;
    @PositiveOrZero
    private BigDecimal dailyLimit;
    private boolean supervisor;
}
