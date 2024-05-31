package rs.edu.raf.repository.racun;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.racun.ExchangeInvoice;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<ExchangeInvoice,Long> {
    List<ExchangeInvoice> findExchangeInvoicesBySenderCurrency(String curr);
    List<ExchangeInvoice> findExchangeInvoicesByToCurrency(String curr);
}
