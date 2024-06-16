package rs.edu.raf.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.order.model.Ponuda;

import java.math.BigDecimal;

@Repository
public interface PonudaRepository extends JpaRepository<Ponuda,Long> {

    @Query(value = "SELECT prihvatiPonudu(:ponudaID);", nativeQuery = true)
    void prihvatiPonudu(@Param("ponudaID") Long id);

    @Query(value = "SELECT bankaaa(:stockTicker,:stockQuantity,:amountOffered)",nativeQuery = true)
    void banka3PrihvataPonudu(@Param("stockTicker") String ticker, @Param("stockQuantity") Long quantity,
                              @Param("amountOffered") BigDecimal amountOffered);


}
