package rs.edu.raf.exchange.service;

import rs.edu.raf.exchange.dto.ExchangeDTO;

import java.util.List;

/**
 * This interface defines methods for managing exchanges.
 */
public interface ExchangeService {

    /**
     * Retrieves an exchange by its name.
     *
     * @param name The name of the exchange to retrieve.
     * @return The ExchangeDTO object representing the retrieved exchange.
     */
    ExchangeDTO getExchangeByExchangeName(String name);

    /**
     * Retrieves all exchanges.
     *
     * @return A list of ExchangeDTO objects representing all exchanges.
     */
    List<ExchangeDTO> getAllExchanges();

    /**
     * Retrieves exchanges by currency.
     *
     * @param currency The currency used in the exchanges to retrieve.
     * @return A list of ExchangeDTO objects representing the exchanges using the specified currency.
     */
    List<ExchangeDTO> getExchangesByCurrency(String currency);

    /**
     * Retrieves open exchanges.
     *
     * @return A list of ExchangeDTO objects representing the open exchanges.
     */
    List<ExchangeDTO> getOpenExchanges();

    /**
     * Retrieves exchanges by polity.
     *
     * @param polity The polity associated with the exchanges to retrieve.
     * @return A list of ExchangeDTO objects representing the exchanges associated with the specified polity.
     */
    List<ExchangeDTO> getExchangesByPolity(String polity);

}
