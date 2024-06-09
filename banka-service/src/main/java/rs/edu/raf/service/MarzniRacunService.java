package rs.edu.raf.service;

import org.springframework.http.ResponseEntity;
import rs.edu.raf.model.dto.PairDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunUpdateDTO;

/**
 * Interface for managing margin accounts.
 */
public interface MarzniRacunService {

    /**
     * Retrieves all margin accounts.
     *
     * @return a response entity containing the list of all margin accounts
     */
    ResponseEntity<?> findAll();

    /**
     * Retrieves all margin accounts for a specific user.
     *
     * @param userId the ID of the user whose margin accounts are to be retrieved
     * @return a response entity containing the list of margin accounts for the specified user
     */
    ResponseEntity<?> findAllByUserId(Long userId);

    /**
     * Creates a new margin account.
     *
     * @param marzniRacunCreateDTO the details of the margin account to be created
     * @return a response entity indicating the result of the create operation
     */
    ResponseEntity<?> createMarzniRacun(MarzniRacunCreateDTO marzniRacunCreateDTO);

    /**
     * Changes the balance of a margin account.
     *
     * @param marzniRacunUpdateDTO the details of the balance change
     * @return a response entity indicating the result of the balance change operation
     */
    ResponseEntity<?> changeBalance(MarzniRacunUpdateDTO marzniRacunUpdateDTO);

    /**
     * Changes the maintenance margin of a margin account.
     *
     * @param marzniRacunUpdateDTO the details of the maintenance margin change
     * @return a response entity indicating the result of the maintenance margin change operation
     */
    ResponseEntity<?> changeMaintenanceMargin(MarzniRacunUpdateDTO marzniRacunUpdateDTO);

    /**
     * Changes the funds from an order in a margin account.
     *
     * @param pairDTO the details of the order funds change
     * @return a response entity indicating the result of the order funds change operation
     */
    ResponseEntity<?> changeFundsFromOrder(PairDTO pairDTO);

    /**
     * Adds funds to a margin account.
     *
     * @param marzniRacunUpdateDTO the details of the funds to be added
     * @return a response entity indicating the result of the add funds operation
     */
    ResponseEntity<?> addFundsToMarzniRacun(MarzniRacunUpdateDTO marzniRacunUpdateDTO);

}

