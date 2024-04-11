package rs.edu.raf.opcija.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.opcija.model.Opcija;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OpcijaRepository extends JpaRepository<Opcija,Long> {


    @Query("SELECT o FROM Opcija o " +
            "WHERE (:stockSymbol IS NULL OR o.ticker = :stockSymbol) " +
            "AND (:expiryDate IS NULL OR o.datumIstekaVazenja = :expiryDate) " +
            "AND (:strikePrice IS NULL OR o.strikePrice = :strikePrice)")
    List<Opcija> findByStockAndDateAndStrike(@Param("stockSymbol") String ticker,
                                             @Param("expiryDate") LocalDateTime expiryDate,
                                             @Param("strikePrice") Double strikePrice);
    //@Param("pageOffset") int pageOffset);


}
