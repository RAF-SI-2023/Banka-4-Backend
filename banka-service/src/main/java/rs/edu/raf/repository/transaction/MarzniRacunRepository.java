package rs.edu.raf.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.racun.MarzniRacun;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarzniRacunRepository extends JpaRepository<MarzniRacun,Long> {

    Optional<MarzniRacun> findByVlasnikAndGrupaHartija(Long userId, String grupaHartija);

    List<MarzniRacun> findAllByVlasnik(Long userId);

    @Query("SELECT mr.id FROM MarzniRacun mr ORDER BY mr.id DESC LIMIT 1")
    Long findTop1ByOrderByIdDesc();

}
