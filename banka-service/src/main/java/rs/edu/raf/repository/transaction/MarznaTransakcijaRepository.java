package rs.edu.raf.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.transaction.MarznaTransakcija;

@Repository
public interface MarznaTransakcijaRepository extends JpaRepository<MarznaTransakcija, Long> {

}
