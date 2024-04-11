package rs.edu.raf.opcija.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KorisnikoveKupljeneOpcije {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long korisnikId;

    private Long opcijaId;

    private Long akcijaId;

    private double akcijaTickerCenaPrilikomIskoriscenja;//stvarna cena akcija prilikom iskoriscenja opcije

    private boolean iskoriscena;//bilo da je put ili call iskorisceno
}