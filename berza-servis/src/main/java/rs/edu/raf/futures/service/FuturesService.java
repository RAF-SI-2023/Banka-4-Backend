package rs.edu.raf.futures.service;

import rs.edu.raf.futures.dto.FutureRequestDto;
import rs.edu.raf.futures.dto.FuturesContractDto;

import java.util.List;

/**
 * This interface defines methods for managing futures contracts.
 */
public interface FuturesService {

    /**
     * Finds futures contracts by type.
     *
     * @param type The type of futures contracts to find.
     * @return A list of FuturesContractDto objects matching the specified type.
     */
    List<FuturesContractDto> findByType(String type);

    /**
     * Finds futures contracts by name.
     *
     * @param name The name of the futures contracts to find.
     * @return A list of FuturesContractDto objects matching the specified name.
     */
    List<FuturesContractDto> findByName(String name);

    /**
     * Finds futures contracts by buyer ID.
     *
     * @param kupacId The ID of the buyer whose futures contracts are to be found.
     * @return A list of FuturesContractDto objects bought by the specified buyer.
     */
    List<FuturesContractDto> findByKupac(Long kupacId);

    /**
     * Buys a futures contract.
     *
     * @param id      The ID of the futures contract to buy.
     * @param kupacId The ID of the buyer who is buying the futures contract.
     * @return The FuturesContractDto object representing the bought futures contract.
     */
    /**
     * Buys a futures contract.
     *
     * @param id the ID of the futures contract
     * @param kupacId the ID of the buyer
     * @param racunId the account ID
     * @return the details of the purchased futures contract
     */
    FuturesContractDto buy(Long id, Long kupacId, String racunId);

    /**
     * Retrieves all future requests for a given worker.
     *
     * @param radnik_id the ID of the worker
     * @return a list of future request details
     */
    List<FutureRequestDto> allRequests(Long radnik_id);

    /**
     * Denies a futures request.
     *
     * @param id the ID of the request to be denied
     */
    void denyRequest(Long id);

    /**
     * Approves a futures request.
     *
     * @param id the ID of the request to be approved
     * @param supervisor_id the ID of the supervisor approving the request
     */
    void approveRequest(Long id, Long supervisor_id);

}
