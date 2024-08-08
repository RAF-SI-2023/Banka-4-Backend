package rs.edu.raf.stocks;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.annotations.GeneratedExternalAPI;
import rs.edu.raf.annotations.GeneratedScheduledOperation;
import rs.edu.raf.stocks.dto.*;
import rs.edu.raf.stocks.exceptions.ApiLimitReachException;
import rs.edu.raf.stocks.exceptions.BadDateInputException;
import rs.edu.raf.stocks.exceptions.StockAlreadyExistsException;
import rs.edu.raf.stocks.exceptions.TickerDoesntExist;
import rs.edu.raf.stocks.mapper.StockMapper;
import rs.edu.raf.stocks.model.Stock;
import rs.edu.raf.stocks.repository.StockRepository;
import rs.edu.raf.stocks.response.*;
import rs.edu.raf.stocks.service.StockService;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    private String alphaVantageApiKey = "OF6BVKZOCXWHD9NS";

    @GeneratedScheduledOperation
    @GeneratedExternalAPI
    @Scheduled(fixedRate = 180000)
    private void scheduledUpdateStock() {
        List<Stock> stocks = stockRepository.findAll();

        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        for (Stock stock : stocks){
            String quoteEndpoint = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + stock.getTicker() + "&apikey=" + alphaVantageApiKey;

            HttpRequest quoteRequest = HttpRequest.newBuilder().uri(URI.create(quoteEndpoint)).build();

            try {
                HttpResponse<String> quoteResponse = client.send(quoteRequest, HttpResponse.BodyHandlers.ofString());

                GlobalQuoteResponse quoteApi = gson.fromJson(quoteResponse.body(), GlobalQuoteResponse.class);
                Quote quoteApiResponse = quoteApi.getGlobalQuote();

                stock.setLastRefresh(quoteApiResponse.getLatestTradingDay());

                if (!quoteApiResponse.getPrice().equals("None")) {
                    stock.setPrice(Float.parseFloat(quoteApiResponse.getPrice()));
                } else {
                    stock.setPrice(0f);
                }

                if (!quoteApiResponse.getHigh().equals("None")) {
                    stock.setHigh(Float.parseFloat(quoteApiResponse.getHigh()));
                } else {
                    stock.setHigh(0f);
                }

                if (!quoteApiResponse.getLow().equals("None")) {
                    stock.setLow(Float.parseFloat(quoteApiResponse.getLow()));
                } else {
                    stock.setLow(0f);
                }

                if (!quoteApiResponse.getChange().equals("None")) {
                    stock.setChange(Float.parseFloat(quoteApiResponse.getChange()));
                } else {
                    stock.setChange(0f);
                }

                if (!quoteApiResponse.getVolume().equals("None")) {
                    stock.setVolume(Long.parseLong(quoteApiResponse.getVolume()));
                } else {
                    stock.setVolume(null);
                }

                if (stock.getVolume() != null) {
                    stock.setDollarVolume(stock.getPrice() * stock.getVolume());
                } else {
                    stock.setDollarVolume(0f);
                }

                stock.setNominalValue(stock.getPrice());
                stock.setInitialMarginCost(stock.getPrice() * 0.5f * 1.1f);
                stock.setMarketCap(stock.getPrice() * stock.getOutstandingShares());
                stock.setMaintenanceMargin(0.5f * stock.getPrice());

                stockRepository.save(stock);

            } catch (Exception e) {
                throw new ApiLimitReachException();
            }
        }
    }

    @Override
    public List<StockDTO> getAllStocks() {
        List<Stock> stocks = stockRepository.findAll();

        return stocks.stream().map(stockMapper::stockToStockDTO).collect(Collectors.toList());
    }

    @Override
    public StockDTO getStockByTicker(String ticker) {
        Stock stock = stockRepository.findByTicker(ticker);

        if (stock == null){
            return null;
        }

        return stockMapper.stockToStockDTO(stock);
    }

    @Override
    public List<StockDTO> getStocksByLastRefresh(String date) {
        if (!date.matches("(?:(?:[0-9]{4})-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01]))|(?:(?:[0-9]{4})\\/(?:0[1-9]|1[0-2])\\/(?:0[1-9]|[12][0-9]|3[01]))$\n"))
        {
            throw new BadDateInputException();
        }

        List<Stock> stocks = stockRepository.findAllByLastRefresh(date);

        return stocks.stream().map(stockMapper::stockToStockDTO).collect(Collectors.toList());
    }

    @Override
    public List<StockDTO> getStocksByExchange(String exchange) {
        List<Stock> stocks = stockRepository.findAllByExchange(exchange);

        if (stocks == null){
            return null;
        }

        return stocks.stream().map(stockMapper::stockToStockDTO).collect(Collectors.toList());
    }

    @GeneratedExternalAPI
    @Override
    public StockDTO addStock(TickerDTO tickerDTO) {

        Stock stock = stockRepository.findByTicker(tickerDTO.getTicker());

        if (stock != null){
            throw new StockAlreadyExistsException();
        }

        HttpClient client = HttpClient.newHttpClient();

        String quoteEndpoint = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + tickerDTO.getTicker() + "&apikey=" + alphaVantageApiKey;
        String overviewEndpoint = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + tickerDTO.getTicker() + "&apikey=" + alphaVantageApiKey;

        HttpRequest quoteRequest = HttpRequest.newBuilder().uri(URI.create(quoteEndpoint)).build();
        HttpRequest overviewRequest = HttpRequest.newBuilder().uri(URI.create(overviewEndpoint)).build();

        try {
            HttpResponse<String> quoteResponse = client.send(quoteRequest, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> overviewResponse = client.send(overviewRequest, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            APILimitReachResponse apiLimitReachResponse = gson.fromJson(quoteResponse.body(), APILimitReachResponse.class);
            GlobalQuoteResponse quoteApi = gson.fromJson(quoteResponse.body(), GlobalQuoteResponse.class);
            CompanyOverviewResponse overviewApiResponse = gson.fromJson(overviewResponse.body(), CompanyOverviewResponse.class);
            Quote quoteApiResponse  = quoteApi.getGlobalQuote();

            if (apiLimitReachResponse.getInformation() == null && (overviewApiResponse.getDescription() == null || quoteApiResponse.getPrice() == null)) {
                throw new TickerDoesntExist();
            }

            Stock newStock = new Stock();

            newStock.setTicker(quoteApiResponse.getSymbol());
            newStock.setNameDescription(overviewApiResponse.getDescription());
            newStock.setExchange(overviewApiResponse.getExchange());
            newStock.setLastRefresh(quoteApiResponse.getLatestTradingDay());

            if (!quoteApiResponse.getPrice().equals("None")) {
                newStock.setPrice(Float.parseFloat(quoteApiResponse.getPrice()));
            } else {
                newStock.setPrice(0f);
            }

            if (!quoteApiResponse.getHigh().equals("None")) {
                newStock.setHigh(Float.parseFloat(quoteApiResponse.getHigh()));
            } else {
                newStock.setHigh(0f);
            }

            if (!quoteApiResponse.getLow().equals("None")) {
                newStock.setLow(Float.parseFloat(quoteApiResponse.getLow()));
            } else {
                newStock.setLow(0f);
            }

            if (!quoteApiResponse.getChange().equals("None")) {
                newStock.setChange(Float.parseFloat(quoteApiResponse.getChange()));
            } else {
                newStock.setChange(0f);
            }

            if (!quoteApiResponse.getVolume().equals("None")) {
                newStock.setVolume(Long.parseLong(quoteApiResponse.getVolume()));
            } else {
                newStock.setVolume(null);
            }

            if (!overviewApiResponse.getSharesOutstanding().equals("None")) {
                newStock.setOutstandingShares(Long.parseLong(overviewApiResponse.getSharesOutstanding()));
            } else {
                newStock.setOutstandingShares(null);
            }

            if (!overviewApiResponse.getDividendYield().equals("None")) {
                newStock.setDividendYield(Float.parseFloat(overviewApiResponse.getDividendYield()));
            } else {
                newStock.setDividendYield(0f);
            }

            if (newStock.getVolume() != null) {
                newStock.setDollarVolume(newStock.getPrice() * newStock.getVolume());
            } else {
                newStock.setDollarVolume(0f);
            }

            newStock.setNominalValue(newStock.getPrice());
            newStock.setInitialMarginCost(newStock.getPrice() * 0.5f * 1.1f);
            newStock.setMarketCap(newStock.getPrice() * newStock.getOutstandingShares());
            newStock.setMaintenanceMargin(0.5f * newStock.getPrice());

            return stockMapper.stockToStockDTO(stockRepository.save(newStock));

        } catch (IOException | InterruptedException e) {
            throw new ApiLimitReachException();
        }
    }

    @Override
    public boolean deleteStock(String ticker) {

        Stock stock = stockRepository.findByTicker(ticker);

        if (stock != null){
            stockRepository.delete(stock);
            return true;
        }

        throw new TickerDoesntExist();
    }

    @GeneratedExternalAPI
    @Override
    public Map<String, StockHistoryInfo> getDailyStockHistory(String ticker) {

        HttpClient client = HttpClient.newHttpClient();

        String dailyHistoryEndpoint = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=" + ticker + "&apikey=" + alphaVantageApiKey;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(dailyHistoryEndpoint)).build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            APILimitReachResponse apiLimitReachResponse = gson.fromJson(response.body(), APILimitReachResponse.class);
            StockDailyResponse stockDailyResponse = gson.fromJson(response.body(), StockDailyResponse.class);

            if (apiLimitReachResponse.getInformation() == null && stockDailyResponse.getTimeSeriesDaily() == null){
                throw new TickerDoesntExist();
            }

            return stockDailyResponse.getTimeSeriesDaily();

        } catch (IOException | InterruptedException e) {
            throw new ApiLimitReachException();
        }
    }

    @GeneratedExternalAPI
    @Override
    public Map<String, StockHistoryInfo> getWeeklyStockHistory(String ticker) {

        HttpClient client = HttpClient.newHttpClient();

        String weeklyHistoryEndpoint = "https://www.alphavantage.co/query?function=TIME_SERIES_WEEKLY&symbol=" + ticker + "&apikey=" + alphaVantageApiKey;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(weeklyHistoryEndpoint)).build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            APILimitReachResponse apiLimitReachResponse = gson.fromJson(response.body(), APILimitReachResponse.class);
            StockWeeklyResponse stockWeeklyResponse = gson.fromJson(response.body(), StockWeeklyResponse.class);

            if (apiLimitReachResponse.getInformation() == null && stockWeeklyResponse.getTimeSeriesWeekly() == null){
                throw new TickerDoesntExist();
            }

            return stockWeeklyResponse.getTimeSeriesWeekly();

        } catch (IOException | InterruptedException e) {
            throw new ApiLimitReachException();
        }
    }

    @GeneratedExternalAPI
    @Override
    public Map<String, StockHistoryInfo> getMonthlyStockHistory(String ticker) {

        HttpClient client = HttpClient.newHttpClient();

        String monthlyHistoryEndpoint = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=" + ticker + "&apikey=" + alphaVantageApiKey;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(monthlyHistoryEndpoint)).build();

        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            APILimitReachResponse apiLimitReachResponse = gson.fromJson(response.body(), APILimitReachResponse.class);
            StockMonthlyResponse stockMonthlyResponse = gson.fromJson(response.body(), StockMonthlyResponse.class);

            if (apiLimitReachResponse.getInformation() == null && stockMonthlyResponse.getTimeSeriesMonthly() == null) {
                throw new TickerDoesntExist();
            }

            return stockMonthlyResponse.getTimeSeriesMonthly();

        } catch (IOException | InterruptedException e) {
            throw new ApiLimitReachException();
        }
    }
}
