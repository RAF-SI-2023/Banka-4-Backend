package rs.edu.raf.order.service;

import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.DodajPonuduDto;
import rs.edu.raf.order.dto.PonudaDTO;
import rs.edu.raf.order.model.StranaPonudaDTO;

import java.util.List;
/**
 * Interface for managing offers and related operations.
 */
public interface PonudaService {

    /**
     * Retrieves all stocks from Banka 3.
     *
     * @return a list of Banka 3 stock details
     */
    List<Banka3StockDTO> dohvatiStokoveBanke3();

    /**
     * Adds a new offer.
     *
     * @param ponudaDTO the details of the offer to be added
     */
    void dodajPonudu(DodajPonuduDto ponudaDTO);

    /**
     * Confirms our offer based on the external offer details.
     *
     * @param stranaPonudaDTO the details of the external offer
     */
    void potvrdiNasuPonudu(StranaPonudaDTO stranaPonudaDTO);

    /**
     * Accepts an offer.
     *
     * @param idPonude the ID of the offer to be accepted
     * @return true if the offer was successfully accepted, false otherwise
     */
    boolean prihvati(Long idPonude);

    /**
     * Rejects an offer.
     *
     * @param idPonude the ID of the offer to be rejected
     */
    void odbij(Long idPonude);

    /**
     * Retrieves all offers.
     *
     * @return a list of offer details
     */
    List<PonudaDTO> svePonude();

}
