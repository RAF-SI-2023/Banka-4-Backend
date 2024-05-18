package rs.edu.raf.mapper;


import rs.edu.raf.dto.OmiljeniKorisnikDTO;
import rs.edu.raf.model.OmiljeniKorisnik;

public class OmiljeniKorisnikMapper {

    public static OmiljeniKorisnikDTO toDTO(OmiljeniKorisnik source) {
        return OmiljeniKorisnikDTO.builder()
                .id(source.getId())
                .idKorisnika(source.getIdKorisnika())
                .brojRacunaPosiljaoca(source.getBrojRacunaPosiljaoca())
                .nazivPrimaoca(source.getNazivPrimaoca())
                .brojRacunaPrimaoca(source.getBrojRacunaPrimaoca())
                .broj(source.getBroj())
                .sifraPlacanja(source.getSifraPlacanja())
                .build();
    }

    public static OmiljeniKorisnik toEntity(OmiljeniKorisnikDTO source) {
        OmiljeniKorisnik omiljeniKorisnik = new OmiljeniKorisnik();
        omiljeniKorisnik.setId(source.getId());
        omiljeniKorisnik.setIdKorisnika(source.getIdKorisnika());
        omiljeniKorisnik.setBrojRacunaPosiljaoca(source.getBrojRacunaPosiljaoca());
        omiljeniKorisnik.setNazivPrimaoca(source.getNazivPrimaoca());
        omiljeniKorisnik.setBrojRacunaPrimaoca(source.getBrojRacunaPrimaoca());
        omiljeniKorisnik.setBroj(source.getBroj());
        omiljeniKorisnik.setSifraPlacanja(source.getSifraPlacanja());

        return omiljeniKorisnik;
    }

}
