package rs.edu.raf.servis;

import rs.edu.raf.dto.OmiljeniKorisnikDTO;

import java.util.List;

/**
 * This interface defines methods for managing favorite users.
 */
public interface OmiljeniKorisnikServis {

    /**
     * Adds a favorite user.
     *
     * @param omiljeniKorisnikDTO The DTO containing information about the favorite user to add.
     * @return The added OmiljeniKorisnikDTO object.
     */
    OmiljeniKorisnikDTO add(OmiljeniKorisnikDTO omiljeniKorisnikDTO);

    /**
     * Edits a favorite user.
     *
     * @param omiljeniKorisnikDTO The DTO containing updated information about the favorite user.
     */
    void edit(OmiljeniKorisnikDTO omiljeniKorisnikDTO);

    /**
     * Deletes a favorite user by ID.
     *
     * @param id The ID of the favorite user to delete.
     */
    void delete(Long id);

    /**
     * Finds favorite users by user ID.
     *
     * @param id The ID of the user whose favorite users are to be retrieved.
     * @return A list of OmiljeniKorisnikDTO objects representing the favorite users of the specified user.
     */
    List<OmiljeniKorisnikDTO> findByIdKorisnika(Long id);

}
