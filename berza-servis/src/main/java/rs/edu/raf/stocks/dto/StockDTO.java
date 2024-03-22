package rs.edu.raf.stocks.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class StockDTO {

    private String ticker;

    private String nameDescription;

    private String exchange;

    @Pattern(regexp = "^(?:(?:[0-9]{4})-(?:0[1-9]|1\\d|2\\d|3[01])-(?:0[1-9]|1[0-2]))|(?:(?:[0-9]{4})\\/(?:0[1-9]|1\\d|2\\d|3[01])\\/(?:0[1-9]|1[0-2]))$", message = "Los format datuma! Mora biti (yyyy-mm-dd)")
    private String lastRefresh;

    @DecimalMin(value = "0.0", inclusive = true)
    private float price;

    @DecimalMin(value = "0.0", inclusive = true)
    private float high;

    @DecimalMin(value = "0.0", inclusive = true)
    private float low;

    private float change;

    @PositiveOrZero
    private Long volume;

    @PositiveOrZero
    private Long outstandingShares;

    private float dividendYield;

    @DecimalMin(value = "0.0", inclusive = true)
    private float dollarVolume;

    @DecimalMin(value = "0.0", inclusive = true)
    private float nominalValue;

    @DecimalMin(value = "0.0", inclusive = true)
    private float initialMarginCost;

    @DecimalMin(value = "0.0", inclusive = true)
    private float marketCap;

    @PositiveOrZero
    private float contractSize;

    @DecimalMin(value = "0.0", inclusive = true)
    private float maintenanceMargin;
}
