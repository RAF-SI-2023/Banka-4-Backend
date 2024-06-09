package rs.edu.raf.repository;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.model.Radnik;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class Seeder implements CommandLineRunner {

    KorisnikRepository korisnikRepository;
    RadnikRepository radnikRepository;

    @Override
    public void run(String... args) throws Exception {
        
    boolean reseed = true;
        try{
            if(!reseed)
                return;
            korisnikRepository.deleteAll();
            
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
            korisnik.setPovezaniRacuni("444000000000000033,444000000000000233,444000000000000011,444000000000000211");
            korisnik.setAktivan(true);
            if(!(korisnikRepository.findByBrojTelefonaAndAktivanIsTrue(korisnik.getBrojTelefona()).isPresent()
                    || korisnikRepository.findByEmailAndAktivanIsTrue(korisnik.getEmail()).isPresent()
                    || korisnikRepository.findByJmbgAndAktivanIsTrue(korisnik.getJmbg()).isPresent()))
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
            korisnik2.setPovezaniRacuni("444000000000000133,444000000000000111");
            korisnik2.setAktivan(true);
            if(!(korisnikRepository.findByBrojTelefonaAndAktivanIsTrue(korisnik2.getBrojTelefona()).isPresent()
                    || korisnikRepository.findByEmailAndAktivanIsTrue(korisnik2.getEmail()).isPresent()
                    || korisnikRepository.findByJmbgAndAktivanIsTrue(korisnik2.getJmbg()).isPresent()))
                korisnikRepository.save(korisnik2);
//
            Radnik radnik = new Radnik();
            radnik.setIme("Petr");
            radnik.setPrezime("Stamenic");
            radnik.setJmbg(String.valueOf(1706999793421L));
            radnik.setDatumRodjenja(929631358000L);
            radnik.setPol("M");
            radnik.setEmail("pera@gmail.rs");
            radnik.setBrojTelefona("+381600176998");
            radnik.setAdresa("Moja adresa");
            radnik.setFirmaId(-1L);
            radnik.setPozicija("PM");
            radnik.setDepartman("RAF");
            radnik.setPassword("$2a$12$LEajHsUJyFisGyUlZx7y0OX4Ue9uB99I/Uz9SxORXkyU7MAMcHPLa");
            radnik.setSaltPassword("S4lt");
            radnik.setPermisije(0b1111111111111111111111111111111111111L);
            radnik.setAktivan(true);
            radnik.setDailyLimit(BigDecimal.valueOf(5000.0));
            radnik.setSupervisor(true);
            radnik.setDailySpent(BigDecimal.ZERO);

            Radnik radnik1 = new Radnik();
            radnik1.setIme("Petr");
            radnik1.setPrezime("Stamenic");
            radnik.setFirmaId(-1L);
            radnik1.setJmbg(String.valueOf(1706999793422L));
            radnik1.setDatumRodjenja(929631358000L);
            radnik1.setPol("M");
            radnik1.setEmail("pera1@gmail.rs");
            radnik1.setBrojTelefona("+381600176999");
            radnik1.setAdresa("Moja adresa");
            radnik1.setPozicija("PM");
            radnik1.setDepartman("RAF");
            radnik1.setPassword("$2a$12$LEajHsUJyFisGyUlZx7y0OX4Ue9uB99I/Uz9SxORXkyU7MAMcHPLa");
            radnik1.setSaltPassword("S4lt");
            radnik1.setPermisije(0b111111111111111111111111111111111111111111L);
            radnik1.setAktivan(true);
            radnik1.setDailyLimit(BigDecimal.valueOf(5000.0));
            radnik1.setSupervisor(false);
            radnik1.setDailySpent(BigDecimal.ZERO);
            if(radnikRepository.findAll().isEmpty()) {
                radnikRepository.save(radnik);
                radnikRepository.save(radnik1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
