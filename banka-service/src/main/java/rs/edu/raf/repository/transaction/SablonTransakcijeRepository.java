package rs.edu.raf.repository.transaction;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.transaction.SablonTransakcije;

import java.math.BigDecimal;

@Repository
public interface SablonTransakcijeRepository extends JpaRepository<SablonTransakcije,Long> {

    @Query(value = "SELECT obrada_transakcije(:brojRacunaUplatioca,:brojRacunaPrimaoca,:iznosUplate,:iznosPrimaocu)",nativeQuery = true)
    boolean obradaTransakcije(@Param("brojRacunaUplatioca") Long racunUplatioca, @Param("brojRacunaPrimaoca") Long racunPrimaoca,
                              @Param("iznosUplate")BigDecimal iznosUplate, @Param("iznosPrimaocu") BigDecimal iznosPrimaocu);
}
