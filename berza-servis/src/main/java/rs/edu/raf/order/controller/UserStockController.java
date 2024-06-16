package rs.edu.raf.order.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.UserStockDto;
import rs.edu.raf.order.dto.UserStockRequest;
import rs.edu.raf.order.model.Order;
import rs.edu.raf.order.service.OrderService;
import rs.edu.raf.order.service.UserStockService;

import java.util.List;

@RestController
@RequestMapping("/user-stocks")
@Tag(name = "UserStocks", description = "Operations related to user stocks.")
@AllArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class UserStockController {

    private final UserStockService userStockService;
    private final OrderService orderService;

    @ApiOperation(value = "Returns user or firm stock.")
    @GetMapping("/{userId}/{ticker}")
    public ResponseEntity<UserStockDto> getUserStock(@PathVariable Long userId, @PathVariable String ticker) {
        UserStockDto userStockDto = userStockService.getUserStock(userId, ticker);
        if (userStockDto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Order> buyOrders = orderService.findAllBuyOrdersForTicker(ticker);
        List<Order> sellOrders = orderService.findAllSellOrdersForTicker(ticker);
        if (!buyOrders.isEmpty()) {
            userStockDto.setCurrentBid(buyOrders.get(0).getLimit());
        }
        if (!sellOrders.isEmpty()) {
            userStockDto.setCurrentAsk(sellOrders.get(0).getLimit());
        }
        return new ResponseEntity<>(userStockDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Returns our banks stocks.")
    @GetMapping("/get-our-banks-stocks")
    public ResponseEntity<List<Banka3StockDTO>> getOurBanksStocks() {
        return new ResponseEntity<>(userStockService.getUserStocks(-1L).stream().map(o->new Banka3StockDTO(o.getQuantity(),o.getTicker())).toList(), HttpStatus.OK);
    }

    @ApiOperation(value = "Returns user or firm stocks.")
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserStockDto>> getUserStocks(@PathVariable Long userId) {
        List<UserStockDto> userStockDtos = userStockService.getUserStocks(userId);
        if (userStockDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (UserStockDto userStockDto : userStockDtos) {
            List<Order> buyOrders = orderService.findAllBuyOrdersForTicker(userStockDto.getTicker());
            List<Order> sellOrders = orderService.findAllSellOrdersForTicker(userStockDto.getTicker());
            if (!buyOrders.isEmpty()) {
                userStockDto.setCurrentBid(buyOrders.get(0).getLimit());
            }
            if (!sellOrders.isEmpty()) {
                userStockDto.setCurrentAsk(sellOrders.get(0).getLimit());
            }
        }
        return new ResponseEntity<>(userStockDtos, HttpStatus.OK);
    }

    @ApiOperation(value = "Changes user or firm stock quantity.")
    @PutMapping
    public ResponseEntity<Boolean> changeUserStockQuantity(@RequestBody UserStockRequest userStockRequest) {
        boolean success = userStockService.changeUserStockQuantity(userStockRequest);
        if (!success) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
