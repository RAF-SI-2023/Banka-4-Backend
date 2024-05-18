package rs.edu.raf.servis.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rs.edu.raf.dto.KorisnikDTO;
import rs.edu.raf.exceptions.MailNotSendException;
import rs.edu.raf.servis.MailServis;

@Service("MailServiceImplementation")
public class MailServiceImplementation implements MailServis {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public boolean posaljiMailZaRegistraciju(KorisnikDTO korisnik, String kod) {
        String text = "";
        if(korisnik.getPol().equals("M")){
            text = ("Poštovani gospodine " + korisnik.getPrezime() + ",");
        }else{
            text = ("Poštovana gospođo " + korisnik.getPrezime() + ",");
        }
        text += "\n\nUspešno ste kreirali Vaš nalog!\nŠaljemo Vam kod za aktivaciju Vašeg naloga.\n" +
                "Aktivacioni kod: " + kod + "\n\n Vaša Banka 4";

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rafbanka4@gmail.com");
            message.setTo(korisnik.getEmail());
            message.setSubject("Aktivacioni kod");
            message.setText(text);
            emailSender.send(message);
            return true;
        }catch (MailException exception) {
            exception.printStackTrace();
        }

        throw new MailNotSendException("Mail za registraciju nije poslat");
    }

    @Override
    public boolean posaljiMailZaPromenuLozinke(KorisnikDTO korisnik, String kod) {
        String text = "";
        if(korisnik.getPol().equals("M")){
            text = ("Poštovani gospodine " + korisnik.getPrezime() + ",");
        }else{
            text = ("Poštovana gospođo " + korisnik.getPrezime() + ",");
        }
        text += "\n\nŠaljemo Vam kod za promenu Vaše lozinke.\n" + "Verifikacioni kod: " + kod + "\n\n Vaša Banka 4";

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rafbanka4@gmail.com");
            message.setTo(korisnik.getEmail());
            message.setSubject("Verifikacioni kod");
            message.setText(text);
            emailSender.send(message);
            return true;
        }catch (MailException exception) {
            exception.printStackTrace();
        }

        throw new MailNotSendException("Mail za promenu lozinke nije poslat");
    }
}
