package rs.edu.raf.opcija.servis.util;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.edu.raf.opcija.model.OpcijaTip;

@Data
@NoArgsConstructor
public class OptionYahooApiMap {

    @NotNull
    private String contractSymbol;

    @NotNull
    private Double strike;

    @NotNull
    private String currency;

    @NotNull
    private Double lastPrice;

    @NotNull
    private Double change;

    @NotNull
    private Double percentChange;

    @NotNull
    private Double bid;

    @NotNull
    private Double ask;

    @NotNull
    private String contractSize;

    @NotNull
    private Long expiration;//broj sekundi od 1970

    @NotNull
    private Long lastTradeDate;//broj sekundi od 1970

    @NotNull
    private Double impliedVolatility;

    @NotNull
    private Boolean inTheMoney;

    @NotNull
    private String ticker;

    @NotNull
    private OpcijaTip opcijaTip;

    private Long volume;

    private Long openInterest;

}
