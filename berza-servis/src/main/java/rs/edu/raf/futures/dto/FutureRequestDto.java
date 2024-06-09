package rs.edu.raf.futures.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FutureRequestDto {
    private Long id;
    private String email;
    private String ime;
    private String prezime;
    private String broj_telefona;
    private Long radnik_id;
    private Long racun_id;
    private FuturesContractDto futuresContractDto;
    private String status;
}
