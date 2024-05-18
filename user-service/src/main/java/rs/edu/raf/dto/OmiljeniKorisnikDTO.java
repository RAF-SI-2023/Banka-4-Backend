package rs.edu.raf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OmiljeniKorisnikDTO {

    private Long id;
    private Long idKorisnika;
    private String  brojRacunaPosiljaoca;
    private String nazivPrimaoca;
    private String brojRacunaPrimaoca;
    private Long broj;
    private String sifraPlacanja;
}
