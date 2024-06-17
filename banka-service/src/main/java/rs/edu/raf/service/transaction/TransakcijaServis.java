package rs.edu.raf.service.transaction;


import rs.edu.raf.model.dto.transaction.NoviPrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.NovaUplataDTO;
import rs.edu.raf.model.dto.transaction.PrenosSredstavaDTO;
import rs.edu.raf.model.dto.transaction.UplataDTO;
import rs.edu.raf.model.entities.transaction.PrenosSredstava;
import rs.edu.raf.model.entities.transaction.SablonTransakcije;
import rs.edu.raf.model.entities.transaction.Uplata;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing transactions.
 */
public interface TransakcijaServis {


    //potrebno je obezbediti detaljan pregled svake transakcije po id
    /**
     * Retrieves a detailed overview of a transfer transaction by its ID.
     *
     * @param id The ID of the transfer transaction.
     * @return The detailed transfer transaction DTO.
     */
    PrenosSredstavaDTO dobaviPrenosSretstavaDTOPoID(String id);

    /**
     * Retrieves a detailed overview of a payment transaction by its ID.
     *
     * @param id The ID of the payment transaction.
     * @return The detailed payment transaction DTO.
     */
    UplataDTO dobaciUplatuSretstavaDTOPoID(String id);

    //////////////////////////////////////////////////

    //DODATO
    //potrebno je napraviti opciju za pregled svih transakcija po id klijenta



    //potrebno je napraviti opciju za pregled svih transakcija po racunu

    //PrenosSredstavaDTO KOJI JE KORISNIK SLAO ILI KOJI MU JE STIGAO
                                                        //BROJ RACUNA NA KOJI SE SALJE NOVAC ODNOSNO NA KOJI STIZE
    /**
     * Retrieves a list of transfer transaction DTOs where the specified account number is the recipient.
     *
     * @param brojPrimaoca The account number of the recipient.
     * @return A list of transfer transaction DTOs.
     */
    List<PrenosSredstavaDTO> dobaviPrenosSretstavaDTOPoBrojuPrimaoca(Long brojPrimaoca);

    /**
     * Retrieves a list of transfer transaction DTOs where the specified account number is the sender.
     *
     * @param brojPosiljaoca The account number of the sender.
     * @return A list of transfer transaction DTOs.
     */
    List<PrenosSredstavaDTO> dobaviPrenosSretstavaDTOPoBrojuPosiljaoca(Long brojPosiljaoca);

    /**
     * Retrieves a list of payment transaction DTOs where the specified account number is the recipient.
     *
     * @param brojPrimaoca The account number of the recipient.
     * @return A list of payment transaction DTOs.
     */
    List<UplataDTO> dobaciUplatuSretstavaDTOPoBrojuPrimaoca(Long brojPrimaoca);

    /**
     * Retrieves a list of payment transaction DTOs where the specified account number is the sender.
     *
     * @param brojPosiljaoca The account number of the sender.
     * @return A list of payment transaction DTOs.
     */
    List<UplataDTO> dobaciUplatuSretstavaDTOPoBrojuPosiljaoca(Long brojPosiljaoca);


    ///////////////////////////////////////////////////////////////////

    /**
     * Saves a transfer of funds based on the provided transfer DTO.
     *
     * @param noviPrenosSredstavaDTO The DTO containing information about the new fund transfer.
     * @return The saved transfer of funds.
     */
    PrenosSredstava sacuvajPrenosSredstava(NoviPrenosSredstavaDTO noviPrenosSredstavaDTO);

    /**
     * Saves a payment based on the provided payment DTO.
     *
     * @param novaUplataDTO The DTO containing information about the new payment.
     * @return The saved payment.
     */
    Uplata sacuvajUplatu(NovaUplataDTO novaUplataDTO);

    /**
     * Retrieves a transfer of funds by its ID.
     *
     * @param id The ID of the transfer of funds to retrieve.
     * @return An optional containing the transfer of funds if found, otherwise empty.
     */
    Optional<PrenosSredstava> vratiPrenosSredstavaPoId(String id);

    /**
     * Retrieves a payment by its ID.
     *
     * @param id The ID of the payment to retrieve.
     * @return An optional containing the payment if found, otherwise empty.
     */
    Optional<Uplata> vratiUplatuPoId(String id);

    /**
     * Retrieves a transfer of funds DTO by its ID.
     *
     * @param id The ID of the transfer of funds DTO to retrieve.
     * @return The transfer of funds DTO.
     */
    PrenosSredstavaDTO vratiPrenosSredstavaDtoPoId(String id);

    /**
     * Retrieves a payment DTO by its ID.
     *
     * @param id The ID of the payment DTO to retrieve.
     * @return The payment DTO.
     */
    UplataDTO vratiUplatuDtoPoId(String id);

    /**
     * Retrieves transfer of funds DTOs by the recipient's account number.
     *
     * @param racunPrimaoca The account number of the recipient.
     * @return A list of transfer of funds DTOs.
     */
    List<PrenosSredstavaDTO> vratiPrenosSredstavaDtoPoRacunuPrimaoca(Long racunPrimaoca);

    /**
     * Retrieves payment DTOs by the recipient's account number.
     *
     * @param racunPrimaoca The account number of the recipient.
     * @return A list of payment DTOs.
     */
    List<UplataDTO> vratiUplataDtoPoRacunuPrimaoca(Long racunPrimaoca);

    /**
     * Retrieves transfer of funds DTOs by the sender's account number.
     *
     * @param racunPosiljaoca The account number of the sender.
     * @return A list of transfer of funds DTOs.
     */
    List<PrenosSredstavaDTO> vratiPrenosSredstavaDtoPoRacunuPosiljaoca(Long racunPosiljaoca);

    /**
     * Retrieves payment DTOs by the sender's account number.
     *
     * @param racunPosiljaoca The account number of the sender.
     * @return A list of payment DTOs.
     */
    List<UplataDTO> vratiUplataDtoPoRacunuPosiljaoca(Long racunPosiljaoca);

    /**
     * Retrieves transfers of funds currently being processed.
     *
     * @return A list of transfers of funds being processed.
     */
    List<PrenosSredstava> vratiPrenosSredstavaUObradi();

    /**
     * Retrieves payments currently being processed.
     *
     * @return A list of payments being processed.
     */
    List<Uplata> vratiUplateUObradi();

    /**
     * Calculates the reserved funds for the specified account.
     *
     * @param idRacuna The ID of the account.
     * @return The amount of reserved funds.
     */
    BigDecimal izracunajRezervisanaSredstva(Long idRacuna);

    /**
     * Retrieves the available funds for the specified account.
     *
     * @param idRacuna The ID of the account.
     * @return The amount of available funds.
     */
    BigDecimal vratiSredstva(Long idRacuna);

    /**
     * Updates the status of a payment transaction.
     *
     * @param idUplate           The ID of the payment transaction.
     * @param status             The new status of the payment transaction.
     * @param vremeIzvrsavanja   The execution time of the payment transaction.
     * @return The updated payment transaction.
     */
    Uplata promeniStatusUplate(String idUplate, String status, Long vremeIzvrsavanja);

    /**
     * Updates the status of a transfer transaction.
     *
     * @param idPrenosaSredstava The ID of the transfer transaction.
     * @param status             The new status of the transfer transaction.
     * @param vremeIzvrsavanja   The execution time of the transfer transaction.
     * @return The updated transfer transaction.
     */
    PrenosSredstava promeniStatusPrenosaSredstava(String idPrenosaSredstava, String status, Long vremeIzvrsavanja);


}
