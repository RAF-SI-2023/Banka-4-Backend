package rs.edu.raf.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.order.model.OTC;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OTCRepository extends JpaRepository<OTC,Long> {
    @Query(value = "SELECT otc_u_banci(:posiljalac,:primalac, :iznos)", nativeQuery = true)
    void prebaciNovac(@Param("posiljalac") Long idPosiljaoca, @Param("primalac") Long idPrimaoca, @Param("iznos")BigDecimal iznos);
}
