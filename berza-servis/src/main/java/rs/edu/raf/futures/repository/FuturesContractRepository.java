package rs.edu.raf.futures.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.futures.model.FuturesContract;

import java.util.List;

@Repository
public interface FuturesContractRepository extends JpaRepository<FuturesContract,Long> {
    List<FuturesContract> findAllByTypeAndKupacIdIsNull(String type);
    List<FuturesContract> findAllByNameAndKupacIdIsNull(String ticker);
    List<FuturesContract> findAllByKupacId(Long kupacId);
    @Transactional
    @Query(value = "SELECT kupi_future_contract(:radnik_id,:future_contract_id,:broj_racuna_id);",nativeQuery = true)
    void kupi_future_contract(@Param("radnik_id")Long radnikId, @Param("future_contract_id")Long id, @Param("broj_racuna_id")Long racun_id);
    @Transactional
    @Query(value = "SELECT confirmFuture()",nativeQuery = true)
    void confirmFuture();
}
