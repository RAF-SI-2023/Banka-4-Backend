package rs.edu.raf.servis;

import rs.edu.raf.model.OneTimePassword;

/**
 * This interface defines methods for generating and validating one-time passwords.
 */
public interface OneTimePasswordService {

    /**
     * Generates a new one-time password for the specified email address.
     *
     * @param email The email address for which to generate the one-time password.
     * @return The generated OneTimePassword object.
     */
    OneTimePassword generateOneTimePassword(String email);

    /**
     * Validates a one-time password for the specified email address.
     *
     * @param email    The email address for which to validate the one-time password.
     * @param password The one-time password to validate.
     * @return true if the password is valid and has not expired, false otherwise.
     */
    boolean validateOneTimePassword(String email, String password);

    /**
     * Cleans up expired one-time passwords from the repository.
     * This method deletes one-time passwords that have expired.
     */
    void cleanupOneTimePasswords();
}

