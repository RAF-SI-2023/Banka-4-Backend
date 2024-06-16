package rs.edu.raf.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.transaction.PrenosSredstava;

import java.util.List;


@Repository
public interface PrenosSredstavaRepository extends JpaRepository<PrenosSredstava, String> {

    List<PrenosSredstava> findAllByStatus(String status);

    List<PrenosSredstava> findAllByRacunPosiljaoca(Long racunPosiljaoca);

    List<PrenosSredstava> findAllByRacunPrimaoca(Long racunPrimaoca);

}
