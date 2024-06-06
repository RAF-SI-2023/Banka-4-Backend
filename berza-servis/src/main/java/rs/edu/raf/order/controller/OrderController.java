package rs.edu.raf.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.edu.raf.order.dto.Banka3StockDTO;
import rs.edu.raf.order.dto.OrderDto;
import rs.edu.raf.order.dto.OrderRequest;
import rs.edu.raf.order.dto.RadnikDTO;
import rs.edu.raf.order.service.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Operations related to orders.")
@AllArgsConstructor
@SecurityRequirement(name = "jwt")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private ObjectMapper objectMapper;

    @ApiOperation(value = "Returns all orders.")
    @GetMapping("/all")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getAllOrders(), HttpStatus.OK);
    }

    @ApiOperation(value = "Returns orders for user.")
    @GetMapping("/{userId}")
    public ResponseEntity<List<OrderDto>> GetOrdersForUser(@PathVariable Long userId) {
        return new ResponseEntity<>(orderService.getOrdersForUser(userId), HttpStatus.OK);
    }

    @ApiOperation(value = "Places order for a firma by a radnik.")
    @PostMapping("/place-order-by-radnik")
    public ResponseEntity<OrderDto> placeOrderByRadnikForFirma(@RequestBody @Validated OrderRequest order) {

        String getRadnikByIdEndpoint = "https://banka-4-dev.si.raf.edu.rs/user-service/api/radnik/id/" + order.getUserId();

        RadnikDTO radnikDTO = null;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest radnikRequest = HttpRequest.newBuilder()
                .uri(URI.create(getRadnikByIdEndpoint))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> radnikRequestResponse = client.send(radnikRequest, HttpResponse.BodyHandlers.ofString());

            radnikDTO = objectMapper.readValue(radnikRequestResponse.body(), RadnikDTO.class);

        } catch (IOException | InterruptedException e) {
            System.out.println("ERROR IN PonudaServiceImpl" + e);
        }

        order.setUserId(radnikDTO.getFirmaId());

        return new ResponseEntity<>(orderService.placeOrder(order), HttpStatus.OK);
    }

    @ApiOperation(value = "Places order.")
    @PostMapping("/place-order")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody @Validated OrderRequest order) {
        return new ResponseEntity<>(orderService.placeOrder(order), HttpStatus.OK);
    }

    @ApiOperation(value = "Approximates order value.")
    @PostMapping("/approximate-order-value")
    public ResponseEntity<BigDecimal> approximateOrderValue(@RequestBody @Validated OrderRequest order) {
        return new ResponseEntity<>(orderService.approximateOrderValue(order), HttpStatus.OK);
    }

    @ApiOperation(value = "Rejects order.")
    @PostMapping("/reject/{orderId}")
    public ResponseEntity<OrderDto> rejectOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.rejectOrder(orderId), HttpStatus.OK);
    }

    @ApiOperation(value = "Accepts order.")
    @PostMapping("/approve/{orderId}")
    public ResponseEntity<OrderDto> acceptOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.acceptOrder(orderId), HttpStatus.OK);
    }

}
