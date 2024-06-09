package rs.edu.raf.service.racun;

import rs.edu.raf.model.dto.racun.*;
import rs.edu.raf.model.entities.racun.DevizniRacun;
import rs.edu.raf.model.entities.racun.Firma;
import rs.edu.raf.model.entities.racun.PravniRacun;
import rs.edu.raf.model.entities.racun.TekuciRacun;

import java.math.BigDecimal;
import java.util.List;
/**
 * Service interface for managing accounts.
 */
public interface RacunServis {

    /**
     * Creates a new foreign currency account based on the provided DTO.
     *
     * @param noviDevizniRacunDTO The DTO containing information about the new foreign currency account.
     * @return The created foreign currency account.
     */
    DevizniRacun kreirajDevizniRacun(NoviDevizniRacunDTO noviDevizniRacunDTO);

    /**
     * Creates a new legal entity account based on the provided DTO.
     *
     * @param noviPravniRacunDTO The DTO containing information about the new legal entity account.
     * @return The created legal entity account.
     */
    PravniRacun kreirajPravniRacun(NoviPravniRacunDTO noviPravniRacunDTO);

    /**
     * Creates a new current account based on the provided DTO.
     *
     * @param noviTekuciRacunDTO The DTO containing information about the new current account.
     * @return The created current account.
     */
    TekuciRacun kreirajTekuciRacun(NoviTekuciRacunDTO noviTekuciRacunDTO);

    /**
     * Lists all accounts of a single user based on the user's ID.
     *
     * @param idKorisnika The ID of the user.
     * @return A list of account DTOs belonging to the user.
     */
    List<RacunDTO> izlistavanjeRacunaJednogKorisnika(Long idKorisnika, String token);

    /**
     * Finds an active account by its ID.
     *
     * @param id The ID of the account to find.
     * @return The active account DTO if found, otherwise null.
     */
    RacunDTO nadjiAktivanRacunPoID(Long id);

    /**
     * Finds an active foreign currency account by its ID.
     *
     * @param id The ID of the foreign currency account to find.
     * @return The active foreign currency account if found, otherwise null.
     */
    DevizniRacun nadjiAktivanDevizniRacunPoID(Long id);

    /**
     * Finds an active legal entity account by its ID.
     *
     * @param id The ID of the legal entity account to find.
     * @return The active legal entity account if found, otherwise null.
     */
    PravniRacun nadjiAktivanPravniRacunPoID(Long id);

    /**
     * Finds an active current account by its ID.
     *
     * @param id The ID of the current account to find.
     * @return The active current account if found, otherwise null.
     */
    TekuciRacun nadjiAktivanTekuciRacunPoID(Long id);

    /**
     * Finds an active account by its account number.
     *
     * @param BrojRacuna The account number to search for.
     * @return The active account DTO if found, otherwise null.
     */
    RacunDTO nadjiAktivanRacunPoBrojuRacuna(Long BrojRacuna);

    /**
     * Finds an active foreign currency account by its account number.
     *
     * @param BrojRacuna The account number to search for.
     * @return The active foreign currency account if found, otherwise null.
     */
    DevizniRacun nadjiAktivanDevizniRacunPoBrojuRacuna(Long BrojRacuna);

    /**
     * Finds an active legal entity account by its account number.
     *
     * @param BrojRacuna The account number to search for.
     * @return The active legal entity account if found, otherwise null.
     */
    PravniRacun nadjiAktivanPravniRacunPoBrojuRacuna(Long BrojRacuna);

    /**
     * Finds an active current account by its account number.
     *
     * @param BrojRacuna The account number to search for.
     * @return The active current account if found, otherwise null.
     */
    TekuciRacun nadjiAktivanTekuciRacunPoBrojuRacuna(Long BrojRacuna);

    /**
     * Associates a legal entity account with a company.
     *
     * @param pravniRacun The legal entity account to associate.
     * @param firma       The company to associate with the legal entity account.
     * @return true if the association is successful, otherwise false.
     */
    boolean dodajPravniRacunFirmi(PravniRacun pravniRacun, Firma firma);

    /**
     * Generates an account number based on the account type.
     *
     * @param tipRacuna The type of the account.
     * @return The generated account number.
     */
    Long generisiBrojRacuna(String tipRacuna);

    /**
     * Finds the account type based on the account number.
     *
     * @param BrojRacuna The account number to search for.
     * @return The type of the account if found, otherwise null.
     */
    String nadjiVrstuRacuna(Long BrojRacuna);

    /**
     * Lists all companies.
     *
     * @return A list of company DTOs.
     */
    List<FirmaDTO> izlistajSveFirme();

    /**
     * Creates a new company based on the provided DTO.
     *
     * @param firma The DTO containing information about the new company.
     * @return The created company.
     */
    Firma kreirajFirmu(NovaFirmaDTO firma);

    /**
     * Deactivates an account based on the account number.
     *
     * @param brojRacuna The account number to deactivate.
     * @return true if the deactivation is successful, otherwise false.
     */
    boolean deaktiviraj(Long brojRacuna);

    /**
     * Finds the user by the account number.
     *
     * @param brojRacuna The account number to search for.
     * @return The ID of the user if found, otherwise null.
     */
    Long nadjiKorisnikaPoBrojuRacuna(Long brojRacuna);

    /**
     * Finds the account by the account number.
     *
     * @param brojRacuna The account number to search for.
     * @return The account object if found, otherwise null.
     */
    Object nadjiRacunPoBrojuRacuna(Long brojRacuna);

    /**
     * Processes a bank transaction at an ATM.
     *
     * @param brojRacuna the account number
     * @param stanje the amount involved in the transaction
     * @return true if the transaction was successful, false otherwise
     */
    boolean bankomat(Long brojRacuna, BigDecimal stanje);

}

