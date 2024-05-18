package rs.edu.raf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.SifrePlacanja;

@Repository
public interface SifrePlacanjaRepository extends JpaRepository<SifrePlacanja, Long> {
//  void saveAll(List<SifrePlacanja> sifrePlacanja);
}
