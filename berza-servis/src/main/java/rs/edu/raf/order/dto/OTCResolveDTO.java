package rs.edu.raf.order.dto;

import lombok.Data;

@Data
public class OTCResolveDTO {

    private Long userId;
    private Long otcId;
    private boolean accept;
}
