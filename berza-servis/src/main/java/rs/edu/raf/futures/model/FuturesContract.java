package rs.edu.raf.futures.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuturesContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer contractSize;
    private String contractUnit;
    private BigDecimal openInterest;
    private Long settlementDate;
    private Double maintenanceMargin;
    private String type;
    private boolean bought;
    private Long kupacId;
    private Long racunId;

    public FuturesContract(String name, Integer contractSize, String contractUnit, Double maintenanceMargin, String type) {
        this.name = name;
        this.contractSize = contractSize;
        this.contractUnit = contractUnit;
        this.maintenanceMargin = maintenanceMargin;
        this.type = type;
        this.price = new BigDecimal(ThreadLocalRandom.current().nextDouble(1500.0,3000.0));
//        this.settlementDate = ThreadLocalRandom.current().nextLong(1718038790000L,1718385308000L);
        Random r = new Random();
        this.settlementDate = r.nextLong(1718385308000L-1718038790000L) + 1718038790000L;
    }
}
