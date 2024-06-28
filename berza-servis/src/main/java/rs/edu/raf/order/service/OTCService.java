package rs.edu.raf.order.service;


import rs.edu.raf.order.dto.OTCDTO;
import rs.edu.raf.order.dto.OTCOfferDTO;
import rs.edu.raf.order.dto.OTCPlaceDTO;
import rs.edu.raf.order.dto.OTCResolveDTO;

import java.util.List;

/**
 * This interface defines methods for managing inner bank otc exchange.
 */
public interface OTCService {

    /**
     * Returns all public otc's.
     *
     * @return List of public otc dto's.
     */
    List<OTCDTO> getAllPublicOTC();

    /**
     * Returns all pending otc offers waiting for confirmation by the user or firm.
     *
     * @param id Users(my) id.
     * @return List of otc's a user or firm should accept or decline.
     */
    List<OTCOfferDTO> getAllPendingOTC(Long id);

    /**
     * Accept or decline a pending otc offer.
     *
     * @param otcResolveDTO As a user with id I accept or decline otc with otcId.
     */
    void resolveOTC(OTCResolveDTO otcResolveDTO);

    /**
     * Make a quantity and price offer for a public available otc.
     *
     * @param otcOfferDTO Dto with ticker name for which we want to buy a provided quantity by a provided price.
     */
    void makeOfferForOTC(OTCOfferDTO otcOfferDTO);

    /**
     * Make a otc(stock) public for users to make otc offers.
     *
     * @param otcPlaceDTO Dto with stock ticker and quantity we want to make public for otc offers.
     */
    void placeOTCPublic(OTCPlaceDTO otcPlaceDTO);
}
