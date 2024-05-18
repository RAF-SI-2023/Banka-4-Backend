package rs.edu.raf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.Korisnik;

import java.util.List;
import java.util.Optional;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {

    Optional<Korisnik> findByEmailAndAktivanIsTrue(String email);

    Optional<Korisnik> findByBrojTelefonaAndAktivanIsTrue(String brojTelefona);

    Optional<Korisnik> findByJmbgAndAktivanIsTrue(String jmbg);

    List<Korisnik> findAllByAktivanIsTrue();

    Optional<Korisnik> findKorisnikByIdAndAktivanIsTrue(Long id);
}
