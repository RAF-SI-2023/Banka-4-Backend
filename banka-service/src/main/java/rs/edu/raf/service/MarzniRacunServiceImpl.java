package rs.edu.raf.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.edu.raf.exceptions.BankAccountNotFoundException;
import rs.edu.raf.exceptions.InsufficientAmountOfStockException;
import rs.edu.raf.exceptions.StockForUsersMarginAccountException;
import rs.edu.raf.model.dto.PairDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunCreateDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunDTO;
import rs.edu.raf.model.dto.racun.MarzniRacunUpdateDTO;
import rs.edu.raf.model.dto.transaction.NoviPrenosSredstavaDTO;
import rs.edu.raf.model.entities.racun.MarzniRacun;
import rs.edu.raf.model.mapper.racun.MarzniRacunMapper;
import rs.edu.raf.repository.transaction.MarzniRacunRepository;
import rs.edu.raf.request.OrderRequest;
import rs.edu.raf.response.OrderResponse;
import rs.edu.raf.response.UserStockDTOResponse;
import rs.edu.raf.service.transaction.TransakcijaServis;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MarzniRacunServiceImpl implements MarzniRacunService{

    @Autowired
    private MarzniRacunRepository marzniRacunRepository;

    @Autowired
    private TransakcijaServis transakcijaServis;

    @Override
    public ResponseEntity<?> findAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(marzniRacunRepository.findAll().stream().map(MarzniRacunMapper::mapToDTO).toList());
    }

    @Override
    public ResponseEntity<?> findALlByUserId(Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(marzniRacunRepository.findAllByVlasnik(userId).stream().map(MarzniRacunMapper::mapToDTO).toList());
    }

    @Override
    public ResponseEntity<?> createMarzniRacun(MarzniRacunCreateDTO marzniRacunCreateDTO) {
        if (marzniRacunRepository.findByVlasnikAndGrupaHartija(marzniRacunCreateDTO.getVlasnik(), marzniRacunCreateDTO.getGrupaHartija()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("MarzniRacun already exists for this user and grupaHartija.");

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(marzniRacunRepository.save(MarzniRacunMapper.mapToEntity(marzniRacunCreateDTO)));
    }

    @Override
    public ResponseEntity<?> changeBalance(MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        Optional<MarzniRacun> optionalMarzniRacun = marzniRacunRepository.findByVlasnikAndGrupaHartija(marzniRacunUpdateDTO.getUserId(), marzniRacunUpdateDTO.getGrupaHartija());

        if (optionalMarzniRacun.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No MarzniRacun found with the specified grupaHartija for this user.");

        MarzniRacun marzniRacun = optionalMarzniRacun.get();

        if (marzniRacun.getMarginCall() && marzniRacunUpdateDTO.getAmount().compareTo(BigDecimal.ZERO) < 0)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Margin call active, can't withdraw funds.");

        BigDecimal newBalance = marzniRacun.getLiquidCash().add(marzniRacunUpdateDTO.getAmount());
        marzniRacun.setLiquidCash(newBalance);

        marzniRacunRepository.save(marzniRacun);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> changeMaintenanceMargin(MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        Optional<MarzniRacun> optionalMarzniRacun = marzniRacunRepository.findByVlasnikAndGrupaHartija(marzniRacunUpdateDTO.getUserId(), marzniRacunUpdateDTO.getGrupaHartija());

        if (optionalMarzniRacun.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No MarzniRacun found with the specified grupaHartija for this user.");

        MarzniRacun marzniRacun = optionalMarzniRacun.get();
        marzniRacun.setMaintenanceMargin(marzniRacunUpdateDTO.getAmount());

        marzniRacunRepository.save(marzniRacun);
        return ResponseEntity.ok().build();
    }

    @Scheduled(cron = "0 0 12 * * ?")
    private void midDayJob() {
        marzniRacunRepository.findAll().stream()
                .filter(marzniRacun -> marzniRacun.getUlozenaSredstva().compareTo(marzniRacun.getMaintenanceMargin()) <= 0)
                .forEach(this::executeCut);
    }

    @Scheduled(cron = "0 0 21 * * ?")
    private void endOfDayJob() {
        marzniRacunRepository.findAll().stream()
                .filter(marzniRacun -> marzniRacun.getUlozenaSredstva().compareTo(marzniRacun.getMaintenanceMargin()) <= 0)
                .forEach(this::executeCut);
    }

    private void executeCut(MarzniRacun marzniRacun) {
        if (marzniRacun.getMarginCall() && marzniRacun.getMaintenanceDeadline().compareTo(System.currentTimeMillis()) <= 0) {
            liquidate(marzniRacun, null);
        } else {
            marzniRacun.setMarginCall(true);
            marzniRacun.setMaintenanceDeadline(System.currentTimeMillis() + 86400000L * 3L);
            marzniRacunRepository.save(marzniRacun);
        }
    }

    @Override
    public ResponseEntity<?> changeFundsFromOrder(PairDTO pairDTO) {

        List<MarzniRacun> marzniRacuni = marzniRacunRepository.findAllByVlasnik(pairDTO.getUserId());

        if (marzniRacuni.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Margin account not found!");
        }

        MarzniRacun marzniRacun = marzniRacuni.get(0);

        marzniRacun.setLiquidCash(marzniRacun.getLiquidCash().add(pairDTO.getValueChange()));

        if (marzniRacun.getMarginCall() && marzniRacun.getUlozenaSredstva().add(marzniRacun.getLiquidCash()).compareTo(marzniRacun.getMaintenanceMargin()) >= 0) {
            marzniRacun.setMarginCall(false);
        } else if (marzniRacun.getMarginCall() && marzniRacun.getMaintenanceDeadline().compareTo(System.currentTimeMillis()) <= 0) {
            liquidate(marzniRacun, null);
        }

        return ResponseEntity.ok().build();
    }

    private void liquidate(MarzniRacun marzniRacun, Map<String, Integer> tickers) {
        // Mapa tickers je String za ticker integer za amount stocka da proda sa tim tickerom
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();

        BigDecimal moneyToGainBySellingStocks = BigDecimal.ZERO;
        // Key - which stock(ticker of stock), Value - quantity of the stock to sell
        Map<String, Integer> tickersOfStocksToSell = new HashMap<>();

        // Ako je prosledjen null tickers onda je liquidate automatski pozvan(iz executeCut() iznad),
        // u suprotnom je korisnik izabrao koje hartije ce da izbrise kroz poziv na kontroler marznog racuna
        if (tickers == null) {
            String getAllUserStocksEndpoint = "localhost:8081/api/user-stocks/" + marzniRacun.getVlasnik();

            List<UserStockDTOResponse> stocksUserOwns = new ArrayList<>();

            HttpRequest stocksRequest = HttpRequest.newBuilder()
                    .uri(URI.create(getAllUserStocksEndpoint))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            try {
                HttpResponse<String> stocksResponse = client.send(stocksRequest, HttpResponse.BodyHandlers.ofString());

                if (stocksResponse.statusCode() == 200) {
                    stocksUserOwns = objectMapper.readValue(stocksResponse.body(), new TypeReference<List<UserStockDTOResponse>>() {
                    });
                } else if (stocksResponse.statusCode() == 404) {
                    throw new StockForUsersMarginAccountException("No stocks for provided user margin account found!");
                }
            } catch (Exception e) {
                System.out.println("Exception from MarzniRacunServiceImpl: " + e);
            }

            stocksUserOwns.sort(Comparator.comparing(UserStockDTOResponse::getCurrentBid));
            BigDecimal amountToBeCovered = marzniRacun.getMaintenanceMargin().subtract(marzniRacun.getUlozenaSredstva().add(marzniRacun.getLiquidCash()));

            for (UserStockDTOResponse stock : stocksUserOwns) {
                Integer quantityNeededToBeSold = amountToBeCovered.divide(stock.getCurrentBid()).intValue();
                if (amountToBeCovered.remainder(stock.getCurrentBid()).compareTo(BigDecimal.ZERO) > 0) {
                    quantityNeededToBeSold += 1;
                }
                Integer quantityThatsGonnaBeSold = Integer.min(quantityNeededToBeSold, stock.getQuantity());

                moneyToGainBySellingStocks.add(stock.getCurrentBid().multiply(new BigDecimal(quantityThatsGonnaBeSold)));
                tickersOfStocksToSell.put(stock.getTicker(), quantityThatsGonnaBeSold);
                if (moneyToGainBySellingStocks.compareTo(amountToBeCovered) >= 0) {
                    break;
                }
            }
        } else {
            String getUserStockEndpoint = "localhost:8081/api/user-stocks/" + marzniRacun.getVlasnik();

            for (Map.Entry<String, Integer> ticker : tickers.entrySet()) {

                UserStockDTOResponse usersStockToSell = new UserStockDTOResponse();

                HttpRequest stocksRequest = HttpRequest.newBuilder()
                        .uri(URI.create(getUserStockEndpoint + "/" + ticker))
                        .header("Content-Type", "application/json")
                        .GET()
                        .build();
                try {
                    HttpResponse<String> stocksResponse = client.send(stocksRequest, HttpResponse.BodyHandlers.ofString());

                    if (stocksResponse.statusCode() == 200) {
                        usersStockToSell = objectMapper.readValue(stocksResponse.body(), UserStockDTOResponse.class);
                    } else if (stocksResponse.statusCode() == 404) {
                        throw new StockForUsersMarginAccountException("Stock with ticker " + ticker + " not found in possession of provided users margin account.");
                    }
                } catch (Exception e) {
                    System.out.println("Exception from MarzniRacunServiceImpl: " + e);
                }

                if (ticker.getValue() > usersStockToSell.getQuantity()) {
                    throw new InsufficientAmountOfStockException("User doesn't own the specified quantity " + ticker.getValue() + " of stock " + ticker.getKey() + ", available amount is " + usersStockToSell.getQuantity() + "!");
                } else if (ticker.getValue() < 1) {
                    throw new InsufficientAmountOfStockException("Quantity of stock to sell must be at least 1!");
                }

                moneyToGainBySellingStocks.add(usersStockToSell.getCurrentBid().multiply(BigDecimal.valueOf(ticker.getValue())));
                tickersOfStocksToSell.put(usersStockToSell.getTicker(), ticker.getValue());
            }
        }

        // Zovi order controller da prodas tickersOfStocksToSell
        // i dodaj pare iz moneyToGainBySellingStocks na marzni racun
        // da izmiris MarginMaintenance i ugasis MarginCall
        String sellStockOrderControllerEndpoint = "localhost:8081/api/orders/place-order";
        Gson gson = new Gson();

        for (Map.Entry<String, Integer> ticker : tickersOfStocksToSell.entrySet()) {
            OrderRequest request = new OrderRequest();
            request.setUserId(marzniRacun.getId());
            request.setTicker(ticker.getKey());
            request.setQuantity(ticker.getValue());
            request.setAction("SELL");
            //dodaj ako je potrebno

            HttpRequest sellStockRequest = HttpRequest.newBuilder()
                    .uri(URI.create(sellStockOrderControllerEndpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(request)))
                    .build();

            try {
                HttpResponse<String> sellStockResponse = client.send(sellStockRequest, HttpResponse.BodyHandlers.ofString());

                OrderResponse response = gson.fromJson(sellStockResponse.body(), OrderResponse.class);
                // uradi nesto sa response?
            } catch (Exception e) {
                System.out.println("Exception from MarzniRacunServiceImpl: " + sellStockRequest);
            }
        }
    }

    @Override
    public ResponseEntity<?> addFundsToMarzniRacun(MarzniRacunUpdateDTO marzniRacunUpdateDTO) {
        Optional<MarzniRacun> optionalMarzniRacun = marzniRacunRepository.findByVlasnikAndGrupaHartija(marzniRacunUpdateDTO.getUserId(), marzniRacunUpdateDTO.getGrupaHartija());

        if (optionalMarzniRacun.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No MarzniRacun found with the specified grupaHartija for this user.");

        MarzniRacun marzniRacun = optionalMarzniRacun.get();

        if (!(marzniRacun.getMarginCall() && marzniRacunUpdateDTO.getAmount().compareTo(marzniRacun.getMaintenanceMargin().subtract(marzniRacun.getUlozenaSredstva().add(marzniRacun.getLiquidCash()))) >= 0))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Margin call is not set or the amount is small and can't cover the maintenance margin.");

        if(marzniRacunRepository.prebaciNaMarzni(marzniRacunUpdateDTO.getAmount(),marzniRacun.getBrojRacuna(),marzniRacun.getId()))
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nedovoljno sredstava za izmirenje margin call-a");
    }
}
