package rs.edu.raf.stocks.service;

import rs.edu.raf.stocks.dto.*;
import rs.edu.raf.stocks.response.StockHistoryInfo;

import java.util.List;
import java.util.Map;

/**
 * This interface provides methods to interact with stock-related data.
 */
public interface StockService {

    /**
     * Retrieves all stocks.
     *
     * @return A list of StockDTO objects representing all stocks.
     */
    List<StockDTO> getAllStocks();

    /**
     * Retrieves a stock by its ticker symbol.
     *
     * @param ticker The ticker symbol of the stock to retrieve.
     * @return The StockDTO object representing the retrieved stock.
     */
    StockDTO getStockByTicker(String ticker);

    /**
     * Retrieves stocks that were last refreshed on the specified date.
     *
     * @param date The date when the stocks were last refreshed.
     * @return A list of StockDTO objects refreshed on the specified date.
     */
    List<StockDTO> getStocksByLastRefresh(String date);

    /**
     * Retrieves stocks traded on the specified exchange.
     *
     * @param exchange The exchange on which the stocks are traded.
     * @return A list of StockDTO objects traded on the specified exchange.
     */
    List<StockDTO> getStocksByExchange(String exchange);

    /**
     * Adds a new stock using its ticker symbol.
     *
     * @param tickerDTO The TickerDTO object containing the ticker symbol of the stock to add.
     * @return The StockDTO object representing the added stock.
     */
    StockDTO addStock(TickerDTO tickerDTO);

    /**
     * Deletes a stock by its ticker symbol.
     *
     * @param ticker The ticker symbol of the stock to delete.
     * @return True if the stock was successfully deleted, otherwise false.
     */
    boolean deleteStock(String ticker);

    /**
     * Retrieves daily stock history for the specified stock.
     *
     * @param ticker The ticker symbol of the stock for which to retrieve daily history.
     * @return A map containing daily stock history information.
     */
    Map<String, StockHistoryInfo> getDailyStockHistory(String ticker);

    /**
     * Retrieves weekly stock history for the specified stock.
     *
     * @param ticker The ticker symbol of the stock for which to retrieve weekly history.
     * @return A map containing weekly stock history information.
     */
    Map<String, StockHistoryInfo> getWeeklyStockHistory(String ticker);

    /**
     * Retrieves monthly stock history for the specified stock.
     *
     * @param ticker The ticker symbol of the stock for which to retrieve monthly history.
     * @return A map containing monthly stock history information.
     */
    Map<String, StockHistoryInfo> getMonthlyStockHistory(String ticker);
}
