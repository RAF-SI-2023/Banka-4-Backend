package rs.edu.raf.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RadnikDTO {

    private Long id;
    private String ime;

    private String prezime;

    private String jmbg;
    
    private Long datumRodjenja;

    private String pol;

    private String email;

    private String brojTelefona;

    private String adresa;

    private String username;

    private String pozicija;

    private String departman;

    private Long permisije;
    private Long firmaId;
    private boolean approvalFlag;
    private BigDecimal dailyLimit;
    private BigDecimal dailySpent;
    private boolean supervisor;
}
