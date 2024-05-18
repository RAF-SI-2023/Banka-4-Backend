package rs.edu.raf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.edu.raf.model.Kod;

import java.util.Optional;

public interface KodRepository extends JpaRepository<Kod,Long> {
    Optional<Kod> findByEmailAndReset(String email, boolean reset);
}
