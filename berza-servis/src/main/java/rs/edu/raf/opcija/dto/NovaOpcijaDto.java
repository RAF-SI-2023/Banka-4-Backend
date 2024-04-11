package rs.edu.raf.opcija.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.opcija.model.OpcijaTip;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NovaOpcijaDto {

    private String ticker;
    private String contractSymbol;
    private double strikePrice;
    private double impliedVolatility;
    private double openInterest;
    private LocalDateTime settlementDate;
    private OpcijaTip optionType;
    private long brojUgovora;


}
