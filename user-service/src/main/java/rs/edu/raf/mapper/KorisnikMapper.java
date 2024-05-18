package rs.edu.raf.mapper;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import rs.edu.raf.dto.IzmenaSifreUzKodDto;
import rs.edu.raf.dto.KorisnikDTO;
import rs.edu.raf.dto.NoviKorisnikDTO;
import rs.edu.raf.dto.RegistrujKorisnikDTO;
import rs.edu.raf.model.Korisnik;
import rs.edu.raf.model.Radnik;
import rs.edu.raf.repository.KorisnikRepository;
import rs.edu.raf.repository.RadnikRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class KorisnikMapper{


    private KorisnikRepository korisnikRepository;
    private RadnikRepository radnikRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public Korisnik noviKorisnikDtoToKorisnik(NoviKorisnikDTO noviKorisnikDTO) {
        Korisnik korisnik = new Korisnik();

        korisnik.setIme(noviKorisnikDTO.getIme());
        korisnik.setPrezime(noviKorisnikDTO.getPrezime());
        korisnik.setJmbg(noviKorisnikDTO.getJmbg());
        korisnik.setDatumRodjenja(noviKorisnikDTO.getDatumRodjenja());
        korisnik.setPol(noviKorisnikDTO.getPol());
        korisnik.setEmail(noviKorisnikDTO.getEmail());
        korisnik.setBrojTelefona(noviKorisnikDTO.getBrojTelefona());
        korisnik.setAdresa(noviKorisnikDTO.getAdresa());
        korisnik.setAktivan(noviKorisnikDTO.isAktivan());

        return korisnik;
    }

    public Korisnik registrujKorisnikDtoToKorisnik(RegistrujKorisnikDTO registrujKorisnikDTO) {

        Optional<Korisnik> korisnik = korisnikRepository.findByEmailAndAktivanIsTrue(registrujKorisnikDTO.getEmail());

        if (korisnik.isPresent()){
            korisnik.get().setPassword(bCryptPasswordEncoder.encode(registrujKorisnikDTO.getPassword()));
            return korisnik.get();
        }

        return null;
    }

    public Radnik registrujRadnikDtoToRadnik(RegistrujKorisnikDTO registrujKorisnikDTO){
        Optional<Radnik> radnik = radnikRepository.findByEmailAndAktivanIsTrue(registrujKorisnikDTO.getEmail());
        if(radnik.isPresent()){
            radnik.get().setPassword(bCryptPasswordEncoder.encode(registrujKorisnikDTO.getPassword()));
            return radnik.get();
        }
        return null;
    }

    public KorisnikDTO korisnikToKorisnikDto(Korisnik korisnik) {
        KorisnikDTO korisnikDTO = new KorisnikDTO();

        korisnikDTO.setId(korisnik.getId());
        korisnikDTO.setIme(korisnik.getIme());
        korisnikDTO.setPrezime(korisnik.getPrezime());
        korisnikDTO.setJmbg(korisnik.getJmbg());
        korisnikDTO.setDatumRodjenja(korisnik.getDatumRodjenja());
        korisnikDTO.setPol(korisnik.getPol());
        korisnikDTO.setEmail(korisnik.getEmail());
        korisnikDTO.setBrojTelefona(korisnik.getBrojTelefona());
        korisnikDTO.setAdresa(korisnik.getAdresa());
        korisnikDTO.setPovezaniRacuni(korisnik.getPovezaniRacuni());

        return korisnikDTO;
    }

    public Korisnik izmenaSifreDtoToKorisnik(IzmenaSifreUzKodDto izmenaSifreUzKodDto) {
        Optional<Korisnik> korisnik = korisnikRepository.findByEmailAndAktivanIsTrue(izmenaSifreUzKodDto.getEmail());
        if(korisnik.isPresent()) {
            korisnik.get().setPassword(bCryptPasswordEncoder.encode(izmenaSifreUzKodDto.getSifra()));
            return korisnik.get();
        }
        return null;
    }

}
