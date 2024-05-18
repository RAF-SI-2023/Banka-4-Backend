package rs.edu.raf.servis;

import rs.edu.raf.dto.KorisnikDTO;

/**
 * This interface defines methods for sending emails.
 */
public interface MailServis {

    /**
     * Sends an email for registration.
     *
     * @param korisnik The user for whom to send the registration email.
     * @param kod      The code associated with the registration email.
     * @return True if the email is successfully sent, otherwise false.
     */
    boolean posaljiMailZaRegistraciju(KorisnikDTO korisnik, String kod);

    /**
     * Sends an email for changing password.
     *
     * @param korisnik The user for whom to send the password change email.
     * @param kod      The code associated with the password change email.
     * @return True if the email is successfully sent, otherwise false.
     */
    boolean posaljiMailZaPromenuLozinke(KorisnikDTO korisnik, String kod);
}
