package rs.edu.raf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.OneTimePassword;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
    List<OneTimePassword> findByEmail(String email);
    void deleteByExpirationBefore(LocalDateTime expiration);
}

