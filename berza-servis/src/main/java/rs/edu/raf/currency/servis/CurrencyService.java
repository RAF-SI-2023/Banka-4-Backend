package rs.edu.raf.currency.servis;

import rs.edu.raf.currency.dto.CurrencyDTO;

import java.util.List;

/**
 * This interface defines methods for managing currencies.
 */
public interface CurrencyService {

    /**
     * Retrieves a currency by its currency code.
     *
     * @param currencyCode The code of the currency to retrieve.
     * @return The CurrencyDTO object representing the retrieved currency.
     */
    CurrencyDTO getCurrencyByCurrencyCode(String currencyCode);

    /**
     * Retrieves all currencies.
     *
     * @return A list of CurrencyDTO objects representing all currencies.
     */
    List<CurrencyDTO> getAllCurrencies();

    /**
     * Retrieves currencies in a specific polity.
     *
     * @param polity The polity for which to retrieve currencies.
     * @return A list of CurrencyDTO objects representing currencies in the specified polity.
     */
    List<CurrencyDTO> getCurrenciesInPolity(String polity);

}
