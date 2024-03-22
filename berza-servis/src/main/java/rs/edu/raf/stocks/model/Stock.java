package rs.edu.raf.stocks.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Entity
@Data
@RedisHash("Stock")
public class Stock implements Serializable {

    @Id
    @Column(unique = true)
    private String ticker;

    private String nameDescription;

    private String exchange;

    private String lastRefresh;

    private float price;

    private float high;

    private float low;

    private float change;

    private Long volume;

    private Long outstandingShares;

    private float dividendYield;

    private float dollarVolume;

    private float nominalValue;

    private float initialMarginCost;

    private float marketCap;

    private float contractSize;

    private float maintenanceMargin;
}
