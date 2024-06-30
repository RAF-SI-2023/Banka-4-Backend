package rs.edu.raf.order.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OTCDTO {
    private Long id;

    private Long sellerId;

    private String ticker;

    private Boolean sellerApproval;

    private Boolean banksApproval;

    private Long buyerId;

    private Integer quantityToBuy;

    private BigDecimal priceOffer;
    private String razlogOdbijanja;
    private Long datumKreiranja;
    private Long datumRealizacije;
    private String opis;
}
