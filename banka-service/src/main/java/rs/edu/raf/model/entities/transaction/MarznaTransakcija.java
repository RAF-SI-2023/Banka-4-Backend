package rs.edu.raf.model.entities.transaction;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MarznaTransakcija {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long marzniId;

    private Long timestamp = System.currentTimeMillis();

    @NotNull
    private Long userId;

    @NotNull
    @NotBlank
    private String opis;

    @NotNull
    @NotBlank
    private String valuta;

    @NotNull
    @NotBlank
    private String tip;

    @NotNull
    private Long ulog;

    @NotNull
    private Long loanValue;

    @NotNull
    private Long maintenanceMargin;

    @NotNull
    private Long kamata;
}
