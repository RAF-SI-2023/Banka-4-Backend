package rs.edu.raf.order.repository.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.edu.raf.order.model.UserStock;
import rs.edu.raf.order.repository.UserStockRepository;

import java.math.BigDecimal;

@Component
public class UserStockRepositorySeeder implements CommandLineRunner {

    @Autowired
    private UserStockRepository userStockRepository;

    @Override
    public void run(String... args) throws Exception {
        // User stockovi za nasu banku Banka4
        userStockRepository.deleteAll();
        UserStock newUserStock = new UserStock();
        newUserStock.setUserId(-1L);
        newUserStock.setTicker("AAPL");
        newUserStock.setQuantity(100);
        newUserStock.setCurrentAsk(new BigDecimal(10));
        newUserStock.setCurrentBid(new BigDecimal(10));

        UserStock newUserStock1 = new UserStock();
        newUserStock1.setUserId(-1L);
        newUserStock1.setTicker("SONY");
        newUserStock1.setQuantity(100);
        newUserStock1.setCurrentAsk(new BigDecimal(10));
        newUserStock1.setCurrentBid(new BigDecimal(10));

        UserStock newUserStock2 = new UserStock();
        newUserStock2.setUserId(-1L);
        newUserStock2.setTicker("NFLX");
        newUserStock2.setQuantity(100);
        newUserStock2.setCurrentAsk(new BigDecimal(10));
        newUserStock2.setCurrentBid(new BigDecimal(10));

        UserStock newUserStock3 = new UserStock();
        newUserStock3.setUserId(-1L);
        newUserStock3.setTicker("SBUX");
        newUserStock3.setQuantity(100);
        newUserStock3.setCurrentAsk(new BigDecimal(10));
        newUserStock3.setCurrentBid(new BigDecimal(10));

        userStockRepository.save(newUserStock);
        userStockRepository.save(newUserStock1);
        userStockRepository.save(newUserStock2);
        userStockRepository.save(newUserStock3);
    }
}
