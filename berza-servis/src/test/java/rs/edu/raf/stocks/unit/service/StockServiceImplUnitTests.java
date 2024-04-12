package rs.edu.raf.stocks.unit.service;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import rs.edu.raf.stocks.service.impl.StockServiceImpl;
import rs.edu.raf.stocks.dto.StockDTO;
import rs.edu.raf.stocks.exceptions.BadDateInputException;
import rs.edu.raf.stocks.mapper.StockMapper;
import rs.edu.raf.stocks.model.Stock;
import rs.edu.raf.stocks.repository.StockRepository;
import rs.edu.raf.stocks.response.CompanyOverviewResponse;
import rs.edu.raf.stocks.response.GlobalQuoteResponse;
import rs.edu.raf.stocks.response.Quote;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceImplUnitTests {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockServiceImpl stockService;

    private List<Stock> createMockStocks() {
        Stock stock1 = new Stock();
        stock1.setTicker("AAPL");
        stock1.setNameDescription("Apple Inc.");
        stock1.setExchange("NASDAQ");
        stock1.setLastRefresh("2024-03-20");
        stock1.setPrice(0.0f);
        stock1.setHigh(0.0f);
        stock1.setLow(0.0f);
        stock1.setChange(0.0f);
        stock1.setVolume(0L);
        stock1.setOutstandingShares(0L);
        stock1.setDividendYield(0.0f);
        stock1.setDollarVolume(0.0f);
        stock1.setNominalValue(0.0f);
        stock1.setInitialMarginCost(0.0f);
        stock1.setMarketCap(0.0f);
        stock1.setContractSize(0.0f);
        stock1.setMaintenanceMargin(0.0f);

        Stock stock2 = new Stock();
        stock2.setTicker("GOOGL");
        stock2.setNameDescription("Alphabet Inc.");
        stock2.setExchange("NASDAQ");
        stock2.setLastRefresh("2024-03-20");
        stock2.setPrice(0.0f);
        stock2.setHigh(0.0f);
        stock2.setLow(0.0f);
        stock2.setChange(0.0f);
        stock2.setVolume(0L);
        stock2.setOutstandingShares(0L);
        stock2.setDividendYield(0.0f);
        stock2.setDollarVolume(0.0f);
        stock2.setNominalValue(0.0f);
        stock2.setInitialMarginCost(0.0f);
        stock2.setMarketCap(0.0f);
        stock2.setContractSize(0.0f);
        stock2.setMaintenanceMargin(0.0f);

        Stock stock3 = new Stock();
        stock3.setTicker("MSFT");
        stock3.setNameDescription("Microsoft Corporation");
        stock3.setExchange("NASDAQ");
        stock3.setLastRefresh("2024-03-20");
        stock3.setPrice(0.0f);
        stock3.setHigh(0.0f);
        stock3.setLow(0.0f);
        stock3.setChange(0.0f);
        stock3.setVolume(0L);
        stock3.setOutstandingShares(0L);
        stock3.setDividendYield(0.0f);
        stock3.setDollarVolume(0.0f);
        stock3.setNominalValue(0.0f);
        stock3.setInitialMarginCost(0.0f);
        stock3.setMarketCap(0.0f);
        stock3.setContractSize(0.0f);
        stock3.setMaintenanceMargin(0.0f);

        Stock stock4 = new Stock();
        stock4.setTicker("AS");
        stock4.setNameDescription("Amer Sports Inc.");
        stock4.setExchange("NYSE");
        stock4.setLastRefresh("2024-03-21");
        stock4.setPrice(0.0f);
        stock4.setHigh(0.0f);
        stock4.setLow(0.0f);
        stock4.setChange(0.0f);
        stock4.setVolume(0L);
        stock4.setOutstandingShares(0L);
        stock4.setDividendYield(0.0f);
        stock4.setDollarVolume(0.0f);
        stock4.setNominalValue(0.0f);
        stock4.setInitialMarginCost(0.0f);
        stock4.setMarketCap(0.0f);
        stock4.setContractSize(0.0f);
        stock4.setMaintenanceMargin(0.0f);

        return List.of(stock1, stock2, stock3, stock4);
    }

    private List<StockDTO> createMockStocksDTO() {
        StockDTO stock1 = new StockDTO();
        stock1.setTicker("AAPL");
        stock1.setNameDescription("Apple Inc.");
        stock1.setExchange("NASDAQ");
        stock1.setLastRefresh("2024-03-20");
        stock1.setPrice(0.0f);
        stock1.setHigh(0.0f);
        stock1.setLow(0.0f);
        stock1.setChange(0.0f);
        stock1.setVolume(0L);
        stock1.setOutstandingShares(0L);
        stock1.setDividendYield(0.0f);
        stock1.setDollarVolume(0.0f);
        stock1.setNominalValue(0.0f);
        stock1.setInitialMarginCost(0.0f);
        stock1.setMarketCap(0.0f);
        stock1.setContractSize(0.0f);
        stock1.setMaintenanceMargin(0.0f);

        StockDTO stock2 = new StockDTO();
        stock2.setTicker("GOOGL");
        stock2.setNameDescription("Alphabet Inc.");
        stock2.setExchange("NASDAQ");
        stock2.setLastRefresh("2024-03-20");
        stock2.setPrice(0.0f);
        stock2.setHigh(0.0f);
        stock2.setLow(0.0f);
        stock2.setChange(0.0f);
        stock2.setVolume(0L);
        stock2.setOutstandingShares(0L);
        stock2.setDividendYield(0.0f);
        stock2.setDollarVolume(0.0f);
        stock2.setNominalValue(0.0f);
        stock2.setInitialMarginCost(0.0f);
        stock2.setMarketCap(0.0f);
        stock2.setContractSize(0.0f);
        stock2.setMaintenanceMargin(0.0f);

        StockDTO stock3 = new StockDTO();
        stock3.setTicker("MSFT");
        stock3.setNameDescription("Microsoft Corporation");
        stock3.setExchange("NASDAQ");
        stock3.setLastRefresh("2024-03-20");
        stock3.setPrice(0.0f);
        stock3.setHigh(0.0f);
        stock3.setLow(0.0f);
        stock3.setChange(0.0f);
        stock3.setVolume(0L);
        stock3.setOutstandingShares(0L);
        stock3.setDividendYield(0.0f);
        stock3.setDollarVolume(0.0f);
        stock3.setNominalValue(0.0f);
        stock3.setInitialMarginCost(0.0f);
        stock3.setMarketCap(0.0f);
        stock3.setContractSize(0.0f);
        stock3.setMaintenanceMargin(0.0f);

        StockDTO stock4 = new StockDTO();
        stock4.setTicker("AS");
        stock4.setNameDescription("Amer Sports Inc.");
        stock4.setExchange("NYSE");
        stock4.setLastRefresh("2024-03-21");
        stock4.setPrice(0.0f);
        stock4.setHigh(0.0f);
        stock4.setLow(0.0f);
        stock4.setChange(0.0f);
        stock4.setVolume(0L);
        stock4.setOutstandingShares(0L);
        stock4.setDividendYield(0.0f);
        stock4.setDollarVolume(0.0f);
        stock4.setNominalValue(0.0f);
        stock4.setInitialMarginCost(0.0f);
        stock4.setMarketCap(0.0f);
        stock4.setContractSize(0.0f);
        stock4.setMaintenanceMargin(0.0f);

        return List.of(stock1, stock2, stock3, stock4);
    }

    private String mockJsonResponse1(){
        return "{\n" +
                "    \"Global Quote\": {\n" +
                "        \"01. symbol\": \"GOOGL\",\n" +
                "        \"02. open\": \"149.4700\",\n" +
                "        \"03. high\": \"150.3700\",\n" +
                "        \"04. low\": \"146.9001\",\n" +
                "        \"05. price\": \"147.6000\",\n" +
                "        \"06. volume\": \"24755597\",\n" +
                "        \"07. latest trading day\": \"2024-03-21\",\n" +
                "        \"08. previous close\": \"148.7400\",\n" +
                "        \"09. change\": \"-1.1400\",\n" +
                "        \"10. change percent\": \"-0.7664%\"\n" +
                "    }\n" +
                "}";
    }

    private String mockJsonResponse2(){
        return "{\n" +
                "            \"Symbol\": \"GOOGL\",\n" +
                "                \"AssetType\": \"Common Stock\",\n" +
                "                \"Name\": \"Alphabet Inc Class A\",\n" +
                "                \"Description\": \"Alphabet Inc. is an American multinational conglomerate headquartered in Mountain View, California. It was created through a restructuring of Google on October 2, 2015, and became the parent company of Google and several former Google subsidiaries. The two co-founders of Google remained as controlling shareholders, board members, and employees at Alphabet. Alphabet is the world's fourth-largest technology company by revenue and one of the world's most valuable companies.\",\n" +
                "                \"CIK\": \"1652044\",\n" +
                "                \"Exchange\": \"NASDAQ\",\n" +
                "                \"Currency\": \"USD\",\n" +
                "                \"Country\": \"USA\",\n" +
                "                \"Sector\": \"TECHNOLOGY\",\n" +
                "                \"Industry\": \"SERVICES-COMPUTER PROGRAMMING, DATA PROCESSING, ETC.\",\n" +
                "                \"Address\": \"1600 AMPHITHEATRE PARKWAY, MOUNTAIN VIEW, CA, US\",\n" +
                "                \"FiscalYearEnd\": \"December\",\n" +
                "                \"LatestQuarter\": \"2023-12-31\",\n" +
                "                \"MarketCapitalization\": \"1841575756000\",\n" +
                "                \"EBITDA\": \"100171997000\",\n" +
                "                \"PERatio\": \"25.45\",\n" +
                "                \"PEGRatio\": \"1.544\",\n" +
                "                \"BookValue\": \"22.74\",\n" +
                "                \"DividendPerShare\": \"None\",\n" +
                "                \"DividendYield\": \"None\",\n" +
                "                \"EPS\": \"5.8\",\n" +
                "                \"RevenuePerShareTTM\": \"24.34\",\n" +
                "                \"ProfitMargin\": \"0.24\",\n" +
                "                \"OperatingMarginTTM\": \"0.288\",\n" +
                "                \"ReturnOnAssetsTTM\": \"0.144\",\n" +
                "                \"ReturnOnEquityTTM\": \"0.274\",\n" +
                "                \"RevenueTTM\": \"307393987000\",\n" +
                "                \"GrossProfitTTM\": \"156633000000\",\n" +
                "                \"DilutedEPSTTM\": \"5.8\",\n" +
                "                \"QuarterlyEarningsGrowthYOY\": \"0.56\",\n" +
                "                \"QuarterlyRevenueGrowthYOY\": \"0.135\",\n" +
                "                \"AnalystTargetPrice\": \"162.23\",\n" +
                "                \"AnalystRatingStrongBuy\": \"13\",\n" +
                "                \"AnalystRatingBuy\": \"30\",\n" +
                "                \"AnalystRatingHold\": \"10\",\n" +
                "                \"AnalystRatingSell\": \"0\",\n" +
                "                \"AnalystRatingStrongSell\": \"0\",\n" +
                "                \"TrailingPE\": \"25.45\",\n" +
                "                \"ForwardPE\": \"21.79\",\n" +
                "                \"PriceToSalesRatioTTM\": \"5.99\",\n" +
                "                \"PriceToBookRatio\": \"6.48\",\n" +
                "                \"EVToRevenue\": \"5.72\",\n" +
                "                \"EVToEBITDA\": \"17.96\",\n" +
                "                \"Beta\": \"1.044\",\n" +
                "                \"52WeekHigh\": \"153.78\",\n" +
                "                \"52WeekLow\": \"99.74\",\n" +
                "                \"50DayMovingAverage\": \"142.88\",\n" +
                "                \"200DayMovingAverage\": \"133.93\",\n" +
                "                \"SharesOutstanding\": \"5893000000\",\n" +
                "                \"DividendDate\": \"None\",\n" +
                "                \"ExDividendDate\": \"None\"\n" +
                "        }";
    }

    private CompanyOverviewResponse mockCompanyOverviewResponse(){
        CompanyOverviewResponse companyOverviewResponse = new CompanyOverviewResponse();
        companyOverviewResponse.setSymbol("GOOGL");
        companyOverviewResponse.setAssetType("Common Stock");
        companyOverviewResponse.setName("Alphabet Inc Class A");
        companyOverviewResponse.setDescription("Alphabet Inc. is an American multinational conglomerate headquartered in Mountain View, California. It was created through a restructuring of Google on October 2, 2015, and became the parent company of Google and several former Google subsidiaries. The two co-founders of Google remained as controlling shareholders, board members, and employees at Alphabet. Alphabet is the world's fourth-largest technology company by revenue and one of the world's most valuable companies.");
        companyOverviewResponse.setCIK("1652044");
        companyOverviewResponse.setExchange("NASDAQ");
        companyOverviewResponse.setCurrency("USD");
        companyOverviewResponse.setCountry("USA");
        companyOverviewResponse.setSector("TECHNOLOGY");
        companyOverviewResponse.setIndustry("SERVICES-COMPUTER PROGRAMMING, DATA PROCESSING, ETC.");
        companyOverviewResponse.setAddress("1600 AMPHITHEATRE PARKWAY, MOUNTAIN VIEW, CA, US");
        companyOverviewResponse.setFiscalYearEnd("December");
        companyOverviewResponse.setLatestQuarter("2023-12-31");
        companyOverviewResponse.setMarketCapitalization("1841575756000");
        companyOverviewResponse.setEBITDA("100171997000");
        companyOverviewResponse.setPERatio("25.45");
        companyOverviewResponse.setPEGRatio("1.544");
        companyOverviewResponse.setBookValue("22.74");
        companyOverviewResponse.setDividendPerShare("None");
        companyOverviewResponse.setDividendYield("None");
        companyOverviewResponse.setEPS("5.8");
        companyOverviewResponse.setRevenuePerShareTTM("24.34");
        companyOverviewResponse.setProfitMargin("0.24");
        companyOverviewResponse.setOperatingMarginTTM("0.288");
        companyOverviewResponse.setReturnOnAssetsTTM("0.144");
        companyOverviewResponse.setReturnOnEquityTTM("0.274");
        companyOverviewResponse.setRevenueTTM("307393987000");
        companyOverviewResponse.setGrossProfitTTM("156633000000");
        companyOverviewResponse.setDilutedEPSTTM("5.8");
        companyOverviewResponse.setQuarterlyEarningsGrowthYOY("0.56");

        return companyOverviewResponse;
    }

    private GlobalQuoteResponse mockGlobalQuoteResponse(){
        GlobalQuoteResponse globalQuoteResponse = new GlobalQuoteResponse();
        Quote quote = new Quote();
        quote.setSymbol("GOOGL");
        quote.setOpen("149.4700");
        quote.setHigh("150.3700");
        quote.setLow("146.9001");
        quote.setPrice("147.6000");
        quote.setVolume("24755597");
        quote.setLatestTradingDay("2024-03-21");
        quote.setPreviousClose("148.7400");
        quote.setChange("-1.1400");
        quote.setChangePercent("-0.7664%");

        globalQuoteResponse.setGlobalQuote(quote);

        return globalQuoteResponse;
    }

    @Test
    public void testGetAllStocks() {

        List<Stock> stocks = createMockStocks();

        List<StockDTO> stocksDTO = createMockStocksDTO();

        given(stockMapper.stockToStockDTO(stocks.get(0))).willReturn(stocksDTO.get(0));
        given(stockMapper.stockToStockDTO(stocks.get(1))).willReturn(stocksDTO.get(1));
        given(stockMapper.stockToStockDTO(stocks.get(2))).willReturn(stocksDTO.get(2));
        given(stockMapper.stockToStockDTO(stocks.get(3))).willReturn(stocksDTO.get(3));

        given(stockRepository.findAll()).willReturn(stocks);

        try{
            List<StockDTO> result = stockService.getAllStocks();
            assertEquals(stocksDTO, result);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetStockByTicker(){

        Stock stock = createMockStocks().get(0);
        StockDTO stockDTO = createMockStocksDTO().get(0);

        given(stockMapper.stockToStockDTO(stock)).willReturn(stockDTO);
        given(stockRepository.findByTicker(stock.getTicker())).willReturn(stock);

        try{
            StockDTO result = stockService.getStockByTicker(stock.getTicker());
            assertEquals(stockDTO, result);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetStocksByLastRefresh(){

        List<Stock> stocks = createMockStocks();
        List<StockDTO> stocksDTO = createMockStocksDTO();

        given(stockMapper.stockToStockDTO(stocks.get(0))).willReturn(stocksDTO.get(0));
        given(stockMapper.stockToStockDTO(stocks.get(1))).willReturn(stocksDTO.get(1));
        given(stockMapper.stockToStockDTO(stocks.get(2))).willReturn(stocksDTO.get(2));

        given(stockRepository.findAllByLastRefresh(stocks.get(0).getLastRefresh())).willReturn(List.of(stocks.get(0), stocks.get(1), stocks.get(2)));

        try{
            List<StockDTO> result = stockService.getStocksByLastRefresh(stocks.get(0).getLastRefresh());
            assertEquals(List.of(stocksDTO.get(0), stocksDTO.get(1), stocksDTO.get(2)), result);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testBadInputGetStocksByLastRefresh(){

        assertThrows(BadDateInputException.class, () -> {
            stockService.getStocksByLastRefresh("2024-03-2");
        });

        assertThrows(BadDateInputException.class, () -> {
            stockService.getStocksByLastRefresh("ASD");
        });

        assertThrows(BadDateInputException.class, () -> {
            stockService.getStocksByLastRefresh("2024-13-01");
        });

        assertThrows(BadDateInputException.class, () -> {
            stockService.getStocksByLastRefresh("2024-1-01");
        });

        assertThrows(BadDateInputException.class, () -> {
            stockService.getStocksByLastRefresh("01-01-2024");
        });
    }

    @Test
    public void testGetStocksByExchange(){

            List<Stock> stocks = createMockStocks();
            List<StockDTO> stocksDTO = createMockStocksDTO();

            given(stockMapper.stockToStockDTO(stocks.get(0))).willReturn(stocksDTO.get(0));
            given(stockMapper.stockToStockDTO(stocks.get(1))).willReturn(stocksDTO.get(1));
            given(stockMapper.stockToStockDTO(stocks.get(2))).willReturn(stocksDTO.get(2));

            given(stockRepository.findAllByExchange(stocks.get(0).getExchange())).willReturn(List.of(stocks.get(0), stocks.get(1), stocks.get(2)));

            try{
                List<StockDTO> result = stockService.getStocksByExchange(stocks.get(0).getExchange());
                assertEquals(List.of(stocksDTO.get(0), stocksDTO.get(1), stocksDTO.get(2)), result);
            } catch (Exception e){
                fail(e.getMessage());
            }
    }

//    @Test
//    public void testAddStock() throws IOException, InterruptedException {
//        HttpClient mockClient = Mockito.mock(HttpClient.class);
//        HttpResponse<String> mockQuoteResponse = Mockito.mock(HttpResponse.class);
//        HttpResponse<String> mockOverviewResponse = Mockito.mock(HttpResponse.class);
//
//        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
//                .thenReturn(mockQuoteResponse)
//                .thenReturn(mockOverviewResponse);
//
//        when(mockQuoteResponse.body()).thenReturn(mockJsonResponse1());
//        when(mockOverviewResponse.body()).thenReturn(mockJsonResponse2());
//
//        Gson gson = Mockito.mock(Gson.class);
//
//        given(stockRepository.findByTicker("GOOGL")).willReturn(null);
//        given(stockRepository.save(createMockStocks().get(1))).willReturn(createMockStocks().get(1));
//        given(stockMapper.stockToStockDTO(createMockStocks().get(1))).willReturn(createMockStocksDTO().get(1));
//        given(gson.fromJson(mockQuoteResponse.body(), GlobalQuoteResponse.class)).willReturn(mockGlobalQuoteResponse());
//        given(gson.fromJson(mockOverviewResponse.body(), CompanyOverviewResponse.class)).willReturn(mockCompanyOverviewResponse());
//
//        TickerDTO tickerDTO = new TickerDTO();
//        tickerDTO.setTicker("GOOGL");
//
//        try{
//            StockDTO result = stockService.addStock(tickerDTO);
//            assertEquals(createMockStocksDTO().get(1) , result);
//        } catch (Exception e){
//            fail(e.getMessage());
//        }
//    }

    @Test
    public void testDeleteStock() {
        Stock stock = createMockStocks().get(0);

        given(stockRepository.findByTicker(stock.getTicker())).willReturn(stock);

        try{
            boolean result = stockService.deleteStock(stock.getTicker());
            assertTrue(result);
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}
