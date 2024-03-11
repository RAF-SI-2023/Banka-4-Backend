package rs.edu.raf.korisnik.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import rs.edu.raf.korisnik.dto.NoviRadnikDTO;
import rs.edu.raf.korisnik.dto.RadnikDTO;
import rs.edu.raf.korisnik.model.Radnik;

@Component
@AllArgsConstructor
public class RadnikMapper {

    public Radnik noviRadnikDtoToRadnik(NoviRadnikDTO noviRadnikDTO){
        Radnik radnik = new Radnik();

        radnik.setIme(noviRadnikDTO.getIme());
        radnik.setPrezime(noviRadnikDTO.getPrezime());
        radnik.setJmbg(noviRadnikDTO.getJmbg());
        radnik.setDatumRodjenja(noviRadnikDTO.getDatumRodjenja());
        radnik.setPol(noviRadnikDTO.getPol());
        radnik.setEmail(noviRadnikDTO.getEmail());
        radnik.setBrojTelefona(noviRadnikDTO.getBrojTelefona());
        radnik.setAdresa(noviRadnikDTO.getAdresa());
        radnik.setUsername(noviRadnikDTO.getUsername());
        radnik.setPassword(noviRadnikDTO.getPassword());
        radnik.setSaltPassword(noviRadnikDTO.getSaltPassword());
        radnik.setPozicija(noviRadnikDTO.getPozicija());
        radnik.setDepartman(noviRadnikDTO.getDepartman());
        radnik.setPermisije(noviRadnikDTO.getPermisije());
        radnik.setAktivan(noviRadnikDTO.isAktivan());

        return radnik;
    }

    public RadnikDTO radnikToRadnikDto(Radnik radnik) {
        RadnikDTO radnikDTO = new RadnikDTO();

        radnikDTO.setIme(radnik.getIme());
        radnikDTO.setPrezime(radnik.getPrezime());
        radnikDTO.setJmbg(radnik.getJmbg());
        radnikDTO.setDatumRodjenja(radnik.getDatumRodjenja());
        radnikDTO.setPol(radnik.getPol());
        radnikDTO.setEmail(radnik.getEmail());
        radnikDTO.setBrojTelefona(radnik.getBrojTelefona());
        radnikDTO.setAdresa(radnik.getAdresa());
        radnikDTO.setUsername(radnik.getUsername());
        radnikDTO.setPozicija(radnik.getPozicija());
        radnikDTO.setDepartman(radnik.getDepartman());
        radnikDTO.setPermisije(radnik.getPermisije());

        return radnikDTO;
    }
}
