package rs.edu.raf.service;

import rs.edu.raf.model.dto.ExchangeRateResponseDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * This interface defines methods for managing exchange rates.
 */
public interface ExchangeRateService {

    /**
     * Retrieves all exchange rates.
     *
     * @return A list of ExchangeRateResponseDto objects representing all exchange rates.
     */
    public List<ExchangeRateResponseDto> getAllExchangeRates();

    /**
     * Converts an amount from one currency to another.
     *
     * @param oldValuteCurrencyCode The currency code of the amount to convert.
     * @param newValuteCurrencyCode The currency code to which the amount will be converted.
     * @param oldValuteAmount       The amount to convert.
     * @return The converted amount as a BigDecimal.
     */
    public BigDecimal convert(String oldValuteCurrencyCode, String newValuteCurrencyCode, BigDecimal oldValuteAmount);

}
