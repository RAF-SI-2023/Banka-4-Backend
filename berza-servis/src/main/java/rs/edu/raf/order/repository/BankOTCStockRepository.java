package rs.edu.raf.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.order.model.BankOTCStock;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface BankOTCStockRepository extends JpaRepository<BankOTCStock, Long> {
    BankOTCStock findByTicker(String ticker);
}