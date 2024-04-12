package rs.edu.raf.servis;

import rs.edu.raf.model.SifrePlacanja;

/**
 * This interface defines methods for managing payment codes.
 */
public interface SifrePlacanjaServis {

    /**
     * Initializes the service.
     */
    void init();

    /**
     * Finds a payment code by ID.
     *
     * @param id The ID of the payment code to find.
     * @return The SifrePlacanja object representing the found payment code.
     */
    SifrePlacanja findById(Long id);

    /**
     * Finds a payment code by its form and basis.
     *
     * @param oblikIOsnov The form and basis of the payment code to find.
     * @return The SifrePlacanja object representing the found payment code.
     */
    SifrePlacanja findByOblikAndOsnov(Integer oblikIOsnov);

}
