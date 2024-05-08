package rs.edu.raf.futures.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FutureContractRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private FuturesContract futuresContract;
    private Long racunId;
    private Long radnikId;
    private String email;
    private String ime;
    private String prezime;
    private String broj_telefona;
    private Long firmaId;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
