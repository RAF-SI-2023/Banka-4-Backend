package rs.edu.raf.stocks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.stocks.dto.*;
import rs.edu.raf.stocks.exceptions.*;
import rs.edu.raf.stocks.response.StockHistoryInfo;
import rs.edu.raf.stocks.service.StockService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stock")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class StockController {

    private StockService stockService;

    @GetMapping("/dailyHistory/{ticker}")
    @Operation(description = "Vraca dnevnu istoriju akcije(stock) za prosledjeni ticker.")
    public ResponseEntity<?> getStockDailyHistory(@PathVariable("ticker") @Parameter(description = "Ticker akcije(stock) za koju se trazi dnevna istorija.") String ticker) {

        Map<String, StockHistoryInfo> history;
        try{
            history = stockService.getDailyStockHistory(ticker);
        }
        catch (TickerDoesntExist e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }
        catch (ApiLimitReachException e){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @GetMapping("/weeklyHistory/{ticker}")
    @Operation(description = "Vraca nedeljnu istoriju akcije(stock) za prosledjeni ticker.")
    public ResponseEntity<?> getStockWeeklyHistory(@PathVariable("ticker") @Parameter(description = "Ticker akcije(stock) za koju se trazi nedeljna istorija.") String ticker) {

        Map<String, StockHistoryInfo> history;
        try{
            history = stockService.getWeeklyStockHistory(ticker);
        }
        catch (TickerDoesntExist e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }
        catch (ApiLimitReachException e){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @GetMapping("/monthlyHistory/{ticker}")
    @Operation(description = "Vraca mesecnu istoriju akcije(stock) za prosledjeni ticker.")
    public ResponseEntity<?> getStockMonthlyHistory(@PathVariable("ticker") @Parameter(description = "Ticker akcije(stock) za koju se trazi mesecna istorija.") String ticker) {

        Map<String, StockHistoryInfo> history;
        try{
            history = stockService.getMonthlyStockHistory(ticker);
        }
        catch (TickerDoesntExist e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }
        catch (ApiLimitReachException e){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(description = "Vraca sve akcije(stocks) sacuvane u bazi.")
    public ResponseEntity<List<StockDTO>> getAllStocks() {
        return new ResponseEntity<>(stockService.getAllStocks(), HttpStatus.OK);
    }

    @GetMapping("/{ticker}")
    @Operation(description = "Vraca sve akcije(stock) sacuvane u bazi za prosledjeni ticker.")
    public ResponseEntity<StockDTO> getStockByTicker(@PathVariable("ticker") @Parameter(description = "Ticker iliti simbol akcije(stock).") String ticker) {
        return new ResponseEntity<>(stockService.getStockByTicker(ticker), HttpStatus.OK);
    }

    @GetMapping("/all/date/{date}")
    @Operation(description = "Vraca sve akcije(stocks) sacuvane u bazi azurirane na dan prosledjenog datuma.")
    public ResponseEntity<?> getAllStocksByDate(@PathVariable("date") @Parameter(description = "Datum u formatu stringa yyyy-mm-dd") String date) {

        List<StockDTO> stockDTO;
        try{
            stockDTO = stockService.getStocksByLastRefresh(date);
        }
        catch (BadDateInputException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(stockDTO, HttpStatus.OK);
    }

    @GetMapping("/all/exchange/{exchange}")
    @Operation(description = "Vraca sve akcije(stocks) sacuvane u bazi za prosledjenu berzu.")
    public ResponseEntity<List<StockDTO>> getStocksByExchange(@PathVariable("exchange") @Parameter(description = "Exchange iliti berza.") String exchange) {
        return new ResponseEntity<>(stockService.getStocksByExchange(exchange), HttpStatus.OK);
    }

    @PostMapping
    @Operation(description = "Dodaje novu akciju(stock) u bazu.")
    public ResponseEntity<?> addStock(@RequestBody @Parameter(description = "Ticker akcije(stock) koju treba dodati.") TickerDTO tickerDTO) {

        StockDTO stockDTO;
        try{
            stockDTO = stockService.addStock(tickerDTO);
        }
        catch (StockAlreadyExistsException | TickerDoesntExist e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }
        catch (ApiLimitReachException e){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(stockDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{ticker}")
    @Operation(description = "Brisanje akcije(stock) iz baze.")
    public ResponseEntity<?> deleteStock(@PathVariable("ticker") @Parameter(description = "Ticker akcije(stock) koju treba obrisati.") String ticker) {

        try{
            stockService.deleteStock(ticker);
        }
        catch (TickerDoesntExist e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(e.getMessage()));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
