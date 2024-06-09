package rs.edu.raf.opcija.servis;

import rs.edu.raf.opcija.dto.NovaOpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaDto;
import rs.edu.raf.opcija.dto.OpcijaKorisnikaDto;
import rs.edu.raf.opcija.model.KorisnikoveKupljeneOpcije;
import rs.edu.raf.opcija.model.OpcijaStanje;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This interface defines methods for managing options.
 */
public interface OpcijaServis {

        /**
         * Retrieves all options.
         *
         * @return A list of OpcijaDto objects representing all options.
         * @throws InterruptedException if interrupted while waiting for the operation to complete.
         */
        List<OpcijaDto> findAll() throws InterruptedException;

        /**
         * Saves a new option.
         *
         * @param option The NovaOpcijaDto object containing information about the new option.
         * @return The saved OpcijaDto object.
         */
        OpcijaDto save(NovaOpcijaDto option);

        /**
         * Finds an option by its ID.
         *
         * @param id The ID of the option to find.
         * @return The OpcijaDto object representing the found option.
         */
        OpcijaDto findById(Long id);

        /**
         * Updates existing options.
         *
         * @throws IOException if an I/O error occurs.
         */
        void azuirajPostojeceOpcije() throws IOException;

        /**
         * Finds options by stock, expiration date, and strike price.
         *
         * @param ticker              The ticker symbol of the stock.
         * @param datumIstekaVazenja The expiration date of the options.
         * @param strikePrice         The strike price of the options.
         * @return A list of OpcijaDto objects matching the criteria.
         */
        List<OpcijaDto> findByStockAndDateAndStrike(String ticker, LocalDateTime datumIstekaVazenja, Double strikePrice);

        /**
         * Executes an option.
         *
         * @param opcijaId The ID of the option to execute.
         * @param userId   The ID of the user executing the option.
         * @return The KorisnikoveKupljeneOpcije object representing the executed option.
         */
        KorisnikoveKupljeneOpcije izvrsiOpciju(Long opcijaId, Long userId);

        /**
         * Checks the state of an option.
         *
         * @param opcijaId The ID of the option to check.
         * @return The OpcijaStanje object representing the state of the option.
         */
        OpcijaStanje proveriStanjeOpcije(Long opcijaId);

        /**
         * Finds puts and calls options by stock ticker.
         *
         * @param ticker The ticker symbol of the stock.
         * @return A map containing lists of puts and calls options.
         */
        Map<String, List<OpcijaDto>> findPutsAndCallsByStockTicker(String ticker);

        /**
         * Finds puts and calls options by stock ticker and expiration date.
         *
         * @param ticker      The ticker symbol of the stock.
         * @param startOfDay  The start date of the expiration day.
         * @param endOfDay    The end date of the expiration day.
         * @return A map containing lists of puts and calls options for the specified expiration date range.
         */
        Map<String, List<OpcijaDto>> findPutsAndCallsByStockTickerAndExpirationDate(String ticker, Date startOfDay, Date endOfDay);

        /**
         * Classifies options by their type (puts or calls).
         *
         * @param ticker The ticker symbol of the stock.
         * @return A map containing lists of puts and calls options classified by their type.
         */
        Map<String, List<OpcijaDto>> classifyOptions(String ticker);

        boolean novaOpcijaKorisnika(OpcijaKorisnikaDto opcijaKorisnikaDto);
}
