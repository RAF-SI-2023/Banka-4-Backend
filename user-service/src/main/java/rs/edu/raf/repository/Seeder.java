package rs.edu.raf.repository;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.model.Radnik;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    KorisnikRepository korisnikRepository;
    RadnikRepository radnikRepository;

    @Override
    public void run(String... args) throws Exception {
        Korisnik korisnik = new Korisnik();
        korisnik.setIme("Petar");
        korisnik.setPrezime("Stamenic");
        korisnik.setJmbg(String.valueOf(1705000793457L));
        korisnik.setDatumRodjenja(958514400000L);
        korisnik.setPol("M");
        korisnik.setEmail("pstamenic7721rn@raf.rs");
        korisnik.setBrojTelefona("+381600176999");
        korisnik.setAdresa("Moja adresa");
        korisnik.setPassword("$2a$12$LEajHsUJyFisGyUlZx7y0OX4Ue9uB99I/Uz9SxORXkyU7MAMcHPLa");
        korisnik.setSaltPassword("S4lt");
        korisnik.setPovezaniRacuni("444000000900000033,444000000920000033");
        korisnik.setAktivan(true);
        korisnikRepository.save(korisnik);

        Korisnik korisnik2 = new Korisnik();
        korisnik2.setIme("Petr");
        korisnik2.setPrezime("Stamenic");
        korisnik2.setJmbg(String.valueOf(1706999793421L));
        korisnik2.setDatumRodjenja(929631358000L);
        korisnik2.setPol("M");
        korisnik2.setEmail("stamenic.petar@gmail.rs");
        korisnik2.setBrojTelefona("+381600176998");
        korisnik2.setAdresa("Moja adresa");
        korisnik2.setPassword("$2a$12$LEajHsUJyFisGyUlZx7y0OX4Ue9uB99I/Uz9SxORXkyU7MAMcHPLa");
        korisnik2.setSaltPassword("S4lt");
        korisnik2.setPovezaniRacuni("444000000910000033");
        korisnik2.setAktivan(true);
        korisnikRepository.save(korisnik2);

        Radnik radnik = new Radnik();
        radnik.setIme("Petr");
        radnik.setPrezime("Stamenic");
        radnik.setJmbg(String.valueOf(1706999793421L));
        radnik.setDatumRodjenja(929631358000L);
        radnik.setPol("M");
        radnik.setEmail("pera@gmail.rs");
        radnik.setBrojTelefona("+381600176998");
        radnik.setAdresa("Moja adresa");
        radnik.setPozicija("PM");
        radnik.setDepartman("RAF");
        radnik.setPassword("$2a$12$LEajHsUJyFisGyUlZx7y0OX4Ue9uB99I/Uz9SxORXkyU7MAMcHPLa");
        radnik.setSaltPassword("S4lt");
        radnik.setPermisije(0b1111111111111111111111L);
        radnik.setAktivan(true);
        radnikRepository.save(radnik);
    }
}
