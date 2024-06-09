package rs.edu.raf.service;

import rs.edu.raf.model.dto.ExchangeRateResponseDto;
import rs.edu.raf.model.entities.racun.ExchangeInvoice;

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
    List<ExchangeRateResponseDto> getAllExchangeRates();

    /**
     * Converts an amount from one currency to another.
     *
     * @param oldValuteCurrencyCode The currency code of the amount to convert.
     * @param newValuteCurrencyCode The currency code to which the amount will be converted.
     * @param oldValuteAmount       The amount to convert.
     * @return The converted amount as a BigDecimal.
     */
    BigDecimal convert(String oldValuteCurrencyCode, String newValuteCurrencyCode, BigDecimal oldValuteAmount);

    /**
     * Retrieves a list of exchange invoices filtered by currency.
     *
     * @param currency the currency code to filter the invoices
     * @return a list of exchange invoices that match the specified currency
     */
    List<ExchangeInvoice> listInvoicesByCurrency(String currency);
}
