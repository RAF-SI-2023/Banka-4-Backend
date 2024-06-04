package rs.edu.raf.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.order.model.Ponuda;

@Repository
public interface PonudaRepository extends JpaRepository<Ponuda,Long> {

}
