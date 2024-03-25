package rs.edu.raf.opcija.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.opcija.model.Korisnik;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik,Long> {
}
