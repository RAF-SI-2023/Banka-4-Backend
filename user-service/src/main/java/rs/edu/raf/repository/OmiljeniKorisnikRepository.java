package rs.edu.raf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.OmiljeniKorisnik;

import java.util.List;

@Repository
public interface OmiljeniKorisnikRepository extends JpaRepository<OmiljeniKorisnik, Long> {
    List<OmiljeniKorisnik> findOmiljeniKorisniksByIdKorisnika(Long idKorisnika);
}
