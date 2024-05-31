package rs.edu.raf.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.racun.Racun;

import java.util.Optional;

@Repository
public interface RacunRepository extends JpaRepository<Racun,Long> {
    Optional<Racun> findByBrojRacuna(Long brojRacuna);
}
