package rs.edu.raf.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.order.model.BankOTCStock;

import java.math.BigDecimal;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface BankOTCStockRepository extends JpaRepository<BankOTCStock, Long> {
    BankOTCStock findByTicker(String ticker);

    @Query(value = "SELECT balansbanka(:iznos)",nativeQuery = true)
    void balansbanka(@Param("iznos") BigDecimal iznos);
}