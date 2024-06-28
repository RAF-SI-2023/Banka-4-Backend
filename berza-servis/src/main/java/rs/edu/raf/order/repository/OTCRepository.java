package rs.edu.raf.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.order.model.OTC;

import java.util.List;

@Repository
public interface OTCRepository extends JpaRepository<OTC,Long> {

    OTC findOTCBySellerIdAndTicker(Long SellerId, String ticker);
}
