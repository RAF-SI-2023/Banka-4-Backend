package rs.edu.raf.service;

import rs.edu.raf.model.dto.CreditRequestDto;
import rs.edu.raf.model.dto.DetailedCreditDto;
import rs.edu.raf.model.entities.CreditRequest;

import java.math.BigDecimal;
import java.util.List;

/**
 * This interface defines methods for managing credit requests and credits.
 */
public interface CreditService {

    /**
     * Applies for credit.
     *
     * @param creditRequestDto The DTO containing information to apply for credit.
     * @return The credit request ID.
     */
    public String applyForCredit(CreditRequestDto creditRequestDto);

    /**
     * Approves a credit request.
     *
     * @param id The ID of the credit request to approve.
     * @return A message indicating the result of the operation.
     */
    public String approveCreditRequest(Long id);

    /**
     * Denies a credit request.
     *
     * @param id The ID of the credit request to deny.
     * @return A message indicating the result of the operation.
     */
    public String dennyCreditRequest(Long id);

    /**
     * Retrieves all credit requests with the specified status.
     *
     * @param status The status of the credit requests to retrieve.
     * @return A list of CreditRequestDto objects representing the credit requests.
     */
    public List<CreditRequestDto> getAllCreditRequests(String status);

    /**
     * Retrieves all credit requests for a specific user with the specified status.
     *
     * @param userId The ID of the user whose credit requests are to be retrieved.
     * @param status The status of the credit requests to retrieve.
     * @return A list of CreditRequestDto objects representing the credit requests.
     */
    public List<CreditRequestDto> getAllCreditRequestForUser(Long userId, String status);

    /**
     * Creates a credit.
     *
     * @param creditRequest The credit request to create.
     * @param tipRacuna     The type of account.
     * @param currency      The currency of the credit.
     * @param salary        The salary of the applicant.
     */
    public void createCredit(CreditRequest creditRequest, String tipRacuna, String currency, BigDecimal salary);

    /**
     * Retrieves detailed information about a credit.
     *
     * @param creditRequestId The ID of the credit request.
     * @return The DetailedCreditDto object representing detailed information about the credit.
     */
    public DetailedCreditDto getDetailedCredit(Long creditRequestId);


    /**
     * Deletes creditRequest by creditRequestId
     *
     * @param creditRequestId
     */
    public void deleteCreditRequest(Long creditRequestId);

    public List<CreditRequest> getAllCreditRequestsRaw(String status);

    public List<CreditRequest> getAllCreditRequestsForUserRaw(Long userId,String status);

    public CreditRequest getCreditRequest(Long creditRequestId);
}
