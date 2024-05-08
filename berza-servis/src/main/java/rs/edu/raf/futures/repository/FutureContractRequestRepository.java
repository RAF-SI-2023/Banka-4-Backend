package rs.edu.raf.futures.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.futures.model.FutureContractRequest;

import java.util.List;

@Repository
public interface FutureContractRequestRepository extends JpaRepository<FutureContractRequest,Long> {
    @Query(value = "SELECT * FROM berza_schema.future_contract_request fcr WHERE fcr.firma_id = (SELECT firma_id FROM user_schema.radnik WHERE id = :id)",nativeQuery = true)
    List<FutureContractRequest> allRequests(@Param("id") Long radnik_id);
    @Query(value = "SELECT approve(:id,:supervisor_id)",nativeQuery = true)
    void approve_request(@Param("id") Long id, @Param("supervisor_id") Long supervisor_id);
}
