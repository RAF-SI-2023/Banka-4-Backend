package rs.edu.raf.futures.service;

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
    FuturesContractDto buy(Long id, Long kupacId);
}
