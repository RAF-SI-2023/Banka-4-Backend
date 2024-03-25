package rs.edu.raf.currency.dto;

import lombok.Data;

@Data
public class InflationDTO {

    private String currency; //CurrencyCode

    private String year;

    private String inflationRate;
}
