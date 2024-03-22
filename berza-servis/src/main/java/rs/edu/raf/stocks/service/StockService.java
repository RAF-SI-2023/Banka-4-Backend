package rs.edu.raf.stocks.service;

import rs.edu.raf.stocks.dto.*;
import rs.edu.raf.stocks.response.StockHistoryInfo;

import java.util.List;
import java.util.Map;

public interface StockService {

    List<StockDTO> getAllStocks();

    StockDTO getStockByTicker(String ticker);

    List<StockDTO> getStocksByLastRefresh(String date);

    List<StockDTO> getStocksByExchange(String exchange);

    StockDTO addStock(TickerDTO tickerDTO);

    boolean deleteStock(String ticker);

    Map<String, StockHistoryInfo> getDailyStockHistory(String ticker);

    Map<String, StockHistoryInfo> getWeeklyStockHistory(String ticker);

    Map<String, StockHistoryInfo> getMonthlyStockHistory(String ticker);
}
