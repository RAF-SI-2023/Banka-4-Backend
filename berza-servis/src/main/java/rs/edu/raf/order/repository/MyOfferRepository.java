package rs.edu.raf.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rs.edu.raf.order.model.MyOffer;
import rs.edu.raf.order.model.OfferStatus;

import java.util.List;

@Repository
@Transactional(isolation = Isolation.SERIALIZABLE)
public interface MyOfferRepository extends JpaRepository<MyOffer, Long> {

    List<MyOffer> findAllByOfferStatus(OfferStatus offerStatus);
}