package rs.edu.raf.opcija.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.opcija.model.KorisnikoveKupljeneOpcije;

import java.util.List;
import java.util.Optional;

@Repository
public interface KorisnikoveKupljeneOpcijeRepository extends JpaRepository<KorisnikoveKupljeneOpcije,Long> {
    Optional<KorisnikoveKupljeneOpcije> findFirstByOpcijaIdAndKorisnikIdAndIskoriscenaFalse(Long opcijaId, Long korisnikId);

    List<KorisnikoveKupljeneOpcije> findAllKorisnikoveKupljeneOpcijeByKorisnikId(Long korisnikId);
}
