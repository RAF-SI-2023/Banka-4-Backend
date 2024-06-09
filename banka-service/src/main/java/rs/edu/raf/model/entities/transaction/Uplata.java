package rs.edu.raf.model.entities.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Document(collection = "uplata")
public class Uplata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long racunPosiljaoca;

    private String nazivPrimaoca;

    private Long racunPrimaoca;

    private BigDecimal iznos;

    private Integer pozivNaBroj;

    private Integer sifraPlacanja;

    private String svrhaPlacanja;

    private String status;

    private Long vremeTransakcije;

    private Long vremeIzvrsavanja;

}
