package rs.edu.raf.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DodavanjeSifreDTO {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email adresa mora biti validna!")
    private String email;
}

