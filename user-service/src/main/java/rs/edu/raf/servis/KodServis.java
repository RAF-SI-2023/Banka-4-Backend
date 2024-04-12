package rs.edu.raf.servis;

/**
 * This interface defines methods for managing codes.
 */
public interface KodServis {

    /**
     * Adds a code to the service.
     *
     * @param email          The email address associated with the code.
     * @param kod            The code to add.
     * @param expirationDate The expiration date of the code.
     * @param reset          A flag indicating whether the code is for resetting purposes.
     */
    void dodajKod(String email, String kod, Long expirationDate, boolean reset);

    /**
     * Checks if a code is valid.
     *
     * @param email The email address associated with the code.
     * @param kod   The code to check.
     * @param reset A flag indicating whether the code is for resetting purposes.
     * @return True if the code is valid, false otherwise.
     */
    boolean dobarKod(String email, String kod, boolean reset);
}
