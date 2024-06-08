package rs.edu.raf.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.Radnik;

import java.util.List;
import java.util.Optional;

@Repository
public interface RadnikRepository extends JpaRepository<Radnik, Long> {
    Optional<Radnik> findByEmailAndAktivanIsTrue(String email);

    List<Radnik> findAllByAktivanIsTrue();

    @Transactional
    @Modifying
    @Query(value = "UPDATE Radnik SET dailySpent = 0")
    void clearDailySpent();
}
