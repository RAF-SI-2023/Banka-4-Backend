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

    private Long stock_id;

    private Boolean sellerApproval;

    private Boolean banksApproval;

    private Long buyerId;

    private Integer quantityToBuy;

    private BigDecimal priceOffer;
    private String razlogOdbijanja;
    private Long datumKreiranja;
    private Long datumRealizacije;
    private String opis;
    private boolean resen;
}
