package rs.edu.raf.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otc")
public class OTC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long sellerId;

    private String ticker;

    private Integer quantity;

    private Boolean sellerApproval;

    private Boolean banksApproval;

    //Buyer sets these fields when he makes an offer
    private Long buyerId;

    private Integer quantityToBuy;

    private BigDecimal priceOffer;
}
