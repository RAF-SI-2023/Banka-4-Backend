package rs.edu.raf.servis;

import rs.edu.raf.dto.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * This interface defines methods for managing users.
 */
public interface KorisnikServis {

    /**
     * Creates a new user.
     *
     * @param noviKorisnikDTO The DTO containing information about the new user.
     * @return The created KorisnikDTO object.
     */
    KorisnikDTO kreirajNovogKorisnika(NoviKorisnikDTO noviKorisnikDTO);

    /**
     * Registers a new user.
     *
     * @param registrujKorisnikDTO The DTO containing information to register the new user.
     * @return True if the user is successfully registered, otherwise false.
     */
    boolean registrujNovogKorisnika(RegistrujKorisnikDTO registrujKorisnikDTO);

    /**
     * Changes the password of a user using a code.
     *
     * @param izmenaSifreUzKodDto The DTO containing information to change the password.
     * @return The updated KorisnikDTO object.
     */
    KorisnikDTO promeniSifruKorisnikaUzKod(IzmenaSifreUzKodDto izmenaSifreUzKodDto);

    /**
     * Changes the password of a user.
     *
     * @param email            The email address of the user.
     * @param izmenaSifreDto   The DTO containing information to change the password.
     * @return The updated KorisnikDTO object.
     */
    KorisnikDTO promeniSifruKorisnika(String email, IzmenaSifreDto izmenaSifreDto);

    /**
     * Creates a new worker.
     *
     * @param noviRadnikDTO The DTO containing information about the new worker.
     * @param firmaId ID of the company
     * @return The created RadnikDTO object.
     */
    RadnikDTO kreirajNovogRadnika(NoviRadnikDTO noviRadnikDTO, Long firmaId);

    /**
     * Edits a user.
     *
     * @param izmenaKorisnikaDTO The DTO containing information to edit the user.
     * @return The updated KorisnikDTO object.
     */
    KorisnikDTO izmeniKorisnika(IzmenaKorisnikaDTO izmenaKorisnikaDTO);

    /**
     * Edits a worker.
     *
     * @param izmenaRadnikaDTO The DTO containing information to edit the worker.
     * @return The updated RadnikDTO object.
     */
    RadnikDTO izmeniRadnika(IzmenaRadnikaDTO izmenaRadnikaDTO);

    /**
     * Lists all active workers.
     *
     * @return A list of RadnikDTO objects representing all active workers.
     */
    List<RadnikDTO> izlistajSveAktivneRadnike();

    /**
     * Lists all active users.
     *
     * @return A list of KorisnikDTO objects representing all active users.
     */
    List<KorisnikDTO> izlistajSveAktivneKorisnike();

    /**
     * Finds an active worker by email.
     *
     * @param email The email address of the worker to find.
     * @return The RadnikDTO object representing the found worker.
     */
    RadnikDTO nadjiAktivnogRadnikaPoEmail(String email);

    /**
     * Finds an active user by email.
     *
     * @param email The email address of the user to find.
     * @return The KorisnikDTO object representing the found user.
     */
    KorisnikDTO nadjiAktivnogKorisnikaPoEmail(String email);

    /**
     * Finds an active user by JMBG.
     *
     * @param jmbg The JMBG of the user to find.
     * @return The KorisnikDTO object representing the found user.
     */
    KorisnikDTO nadjiAktivnogKorisnikaPoJMBG(String jmbg);

    /**
     * Finds an active user by phone number.
     *
     * @param brojTelefona The phone number of the user to find.
     * @return The KorisnikDTO object representing the found user.
     */
    KorisnikDTO nadjiAktivnogKorisnikaPoBrojuTelefona(String brojTelefona);

    /**
     * Finds a user by ID.
     *
     * @param id The ID of the user to find.
     * @return The KorisnikDTO object representing the found user.
     */
    KorisnikDTO findUserById(Long id);

    /**
     * Adds an account to a user.
     *
     * @param userId        The ID of the user.
     * @param accountNumber The account number to add.
     * @return True if the account is successfully added, otherwise false.
     */
    boolean addAccountToUser(Long userId, Long accountNumber);
    RadnikDTO resetLimit(Long radnikId, Long id);
    void updateDailySpent(Long id, BigDecimal price);
}
