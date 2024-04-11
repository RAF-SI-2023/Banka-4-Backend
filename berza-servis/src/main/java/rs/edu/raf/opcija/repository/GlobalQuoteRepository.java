package rs.edu.raf.opcija.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.opcija.model.GlobalQuote;

import java.util.Optional;

@Repository
public interface GlobalQuoteRepository extends JpaRepository<GlobalQuote,Long> {
    Optional<GlobalQuote> findFirstByTicker(String ticker);
    Optional<GlobalQuote> findByTicker(String ticker);

}
