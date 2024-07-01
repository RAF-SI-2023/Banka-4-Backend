package rs.edu.raf.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_stocks")
public class UserStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private Integer quantity;
    private Integer publicQuantity = 0;

    @Column(nullable = false)
    private BigDecimal currentBid;

    @Column(nullable = false)
    private BigDecimal currentAsk;

}
