package rs.edu.raf.stocks.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import rs.edu.raf.stocks.model.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class StockRepository{

    private RedisTemplate template;

    public StockRepository(RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public Stock save(Stock stock) {
        template.opsForHash().put("stock", stock.getTicker(), stock);

        template.opsForList().rightPush("stock:exchange:" + stock.getExchange(), stock);

        template.opsForList().rightPush("stock:lastRefresh:" + stock.getLastRefresh(), stock);

        return stock;
    }

    public boolean delete(Stock stock){
        if(template.opsForHash().hasKey("stock", stock.getTicker())){
            template.opsForHash().delete("stock", stock.getTicker());
            template.opsForList().remove("stock:exchange:" + stock.getExchange(), 0, stock);
            template.opsForList().remove("stock:lastRefresh:" + stock.getLastRefresh(), 0, stock);

            return true;
        }
        return false;
    }

    public boolean deleteByTicker(String ticker){
        if(template.opsForHash().hasKey("stock", ticker)){
            Stock stock = (Stock) template.opsForHash().get("stock", ticker);

            template.opsForHash().delete("stock", stock.getTicker());
            template.opsForList().remove("stock:exchange:" + stock.getExchange(), 0, stock);
            template.opsForList().remove("stock:lastRefresh:" + stock.getLastRefresh(), 0, stock);

            return true;
        }
        return false;
    }

    public List<Stock> findAll() {
        return (List<Stock>) template.opsForHash().values("stock");
    }

    public Stock findByTicker(String ticker) {
        return (Stock) template.opsForHash().get("stock", ticker);
    }

    public List<Stock> findAllByExchange(String exchange){
        exchange = exchange.toUpperCase();
        Set<String> keys = template.keys("stock:exchange:" + exchange + "*");

        List<Stock> stocks = new ArrayList<>();
        for (String key : keys) {
            List<Stock> stocksForKey = template.opsForList().range(key, 0, -1);
            stocks.addAll(stocksForKey);
        }
        return stocks;
    }

    public List<Stock> findAllByLastRefresh(String lastRefresh){
        Set<String> keys = template.keys("stock:lastRefresh:" + lastRefresh + "*");

        List<Stock> stocks = new ArrayList<>();
        for (String key : keys) {
            List<Stock> stocksForKey = template.opsForList().range(key, 0, -1);
            stocks.addAll(stocksForKey);
        }
        return stocks;
    }
}
