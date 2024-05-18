package rs.edu.raf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SifrePlacanjaDTO {

    private Long id;
    private Integer oblikIOsnov;
    private String opisPlacanja;

}
