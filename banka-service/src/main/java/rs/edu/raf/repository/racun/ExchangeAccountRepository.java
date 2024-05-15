package rs.edu.raf.repository.racun;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.edu.raf.model.entities.racun.ExchangeAccount;

import java.util.Optional;

@Repository
public interface ExchangeAccountRepository extends JpaRepository<ExchangeAccount, Long> {
    Optional<ExchangeAccount> findExchangeAccountByCurrency(String currency);
}
