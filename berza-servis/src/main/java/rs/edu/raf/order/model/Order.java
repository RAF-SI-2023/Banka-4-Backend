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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long radnikId;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "orderLimit")
    private BigDecimal limit;

    private BigDecimal stop;

    @Column(nullable = false)
    private boolean allOrNone;

    @Column(nullable = false)
    private boolean margin;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    private Long lastModified;

}
