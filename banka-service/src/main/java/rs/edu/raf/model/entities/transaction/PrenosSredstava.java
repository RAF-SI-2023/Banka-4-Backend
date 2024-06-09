package rs.edu.raf.model.entities.transaction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "prenos_sredstava")
//@Document(collection = "prenos_sredstava")
@AllArgsConstructor
@NoArgsConstructor
public class PrenosSredstava {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long racunPosiljaoca;

    
    private Long racunPrimaoca;

    
    private BigDecimal iznos;

    
    private Long vreme;

    
    private String status;

    private Long vremeIzvrsavanja = null;

}
