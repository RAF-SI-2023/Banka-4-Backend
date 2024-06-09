package rs.edu.raf.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.order.model.MojePonudeBanci3;

@Repository
public interface MojePonudeBanci3Repository extends JpaRepository<MojePonudeBanci3,Long> {
}
