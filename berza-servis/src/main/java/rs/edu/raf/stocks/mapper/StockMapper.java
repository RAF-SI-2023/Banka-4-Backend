package rs.edu.raf.stocks.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.stocks.dto.StockDTO;
import rs.edu.raf.stocks.model.Stock;

@Component
public class StockMapper {

    public StockDTO stockToStockDTO(Stock stock){
        StockDTO stockDTO = new StockDTO();

        stockDTO.setTicker(stock.getTicker());
        stockDTO.setNameDescription(stock.getNameDescription());
        stockDTO.setExchange(stock.getExchange());
        stockDTO.setLastRefresh(stock.getLastRefresh());
        stockDTO.setPrice(stock.getPrice());
        stockDTO.setHigh(stock.getHigh());
        stockDTO.setLow(stock.getLow());
        stockDTO.setChange(stock.getChange());
        stockDTO.setVolume(stock.getVolume());
        stockDTO.setOutstandingShares(stock.getOutstandingShares());
        stockDTO.setDividendYield(stock.getDividendYield());
        stockDTO.setDollarVolume(stock.getDollarVolume());
        stockDTO.setNominalValue(stock.getNominalValue());
        stockDTO.setInitialMarginCost(stock.getInitialMarginCost());
        stockDTO.setMarketCap(stock.getMarketCap());
        stockDTO.setContractSize(stock.getContractSize());
        stockDTO.setMaintenanceMargin(stock.getMaintenanceMargin());

        return stockDTO;
    }
}
