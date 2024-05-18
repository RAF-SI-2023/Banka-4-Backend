package rs.edu.raf.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "SIFRE_PLACANJA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SifrePlacanja implements Serializable {

    @Serial
    private static final long serialVersionUID = 4378291499382768950L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "OBLIK_I_OSNOV")
    private Integer oblikIOsnov;
    @Column(name = "OPIS_PLACANJA")
    private String opisPlacanja;

}
