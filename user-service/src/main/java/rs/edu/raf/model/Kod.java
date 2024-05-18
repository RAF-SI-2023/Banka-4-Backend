package rs.edu.raf.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Kod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String code;

    //postavljen na datum izdavanja koda preko System.currentTimeMillis+15minuta_kao_milisec
    //istek se uporedjuje preko expirationDate>=System.currentTimeMillis
    private Long expirationDate;

    private boolean reset;

    public Kod(String email, String code, Long expirationDate, boolean reset) {
        this.email = email;
        this.code = code;
        this.expirationDate = expirationDate;
        this.reset = reset;
    }
}
