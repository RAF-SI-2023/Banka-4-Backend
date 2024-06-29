package rs.edu.raf.order.service.impl;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import rs.edu.raf.order.dto.OrderDto;
import rs.edu.raf.order.dto.OrderRequest;
import rs.edu.raf.order.dto.PairDTO;
import rs.edu.raf.order.dto.UserStockRequest;
import rs.edu.raf.order.model.Enums.Action;
import rs.edu.raf.order.model.Enums.Type;
import rs.edu.raf.order.model.Order;
import rs.edu.raf.order.repository.OrderRepository;
import rs.edu.raf.order.service.OrderService;
import rs.edu.raf.order.service.UserStockService;
import rs.edu.raf.order.service.mapper.OrderMapper;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Data
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserStockService userStockService;

    @Override
    public OrderDto placeOrder(OrderRequest orderRequest) {
        return OrderMapper.toDto(orderRepository.save(OrderMapper.mapOrderRequestToOrder(orderRequest)));
//        return (orderRequest.getAction().equals(Action.BUY)) ? placeBuyOrder(OrderMapper.mapOrderRequestToOrder(orderRequest)) : placeSellOrder(OrderMapper.mapOrderRequestToOrder(orderRequest));
    }

    @Override
    public OrderDto rejectOrder(Long orderId) {
        OrderDto result = null;
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getStatus().equals("PENDING")) {
                order.setStatus("REJECTED");
                result = OrderMapper.toDto(orderRepository.save(order));
            }
        }
        return result;
    }

    @Override
    public OrderDto acceptOrder(Long orderId, String token) {
        OrderDto result = null;
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (order.getStatus().equals("PENDING")) {
                order.setStatus("ACCEPTED");
                if (order.getAction().equals(Action.BUY)) placeBuyOrder(order, token);
                else placeSellOrder(order, token);
                result = OrderMapper.toDto(orderRepository.save(order));
            }
        }
        return result;
    }

    private OrderDto placeBuyOrder(Order buyOrder, String token) {
        orderRepository.save(buyOrder);
        checkStopOrderAndStopLimitOrder(token);

        if (buyOrder.getType().equals(Type.MARKET_ORDER) || buyOrder.getType().equals(Type.LIMIT_ORDER)) {

            // check user balance

            // if available then reserve balance

            List<Order> sellOrders = findAllSellOrdersForTicker(buyOrder.getTicker());
            BigDecimal totalValueChange = BigDecimal.ZERO;
            Map<Order, Integer> matchedSellOrders = new HashMap<>();

            for (Order sellOrder : sellOrders) {
                if (buyOrder.getQuantity() == 0 || (buyOrder.getType().equals(Type.LIMIT_ORDER) && buyOrder.getLimit().compareTo(sellOrder.getLimit()) <= 0)) break;

                if (sellOrder.getQuantity() > buyOrder.getQuantity()) {
                    sellOrder.setQuantity(sellOrder.getQuantity() - buyOrder.getQuantity());
                    totalValueChange = totalValueChange.add(sellOrder.getLimit().multiply(new BigDecimal(buyOrder.getQuantity())));
                    matchedSellOrders.put(sellOrder, buyOrder.getQuantity());
                    buyOrder.setQuantity(0);
                } else {
                    buyOrder.setQuantity(buyOrder.getQuantity() - sellOrder.getQuantity());
                    totalValueChange = totalValueChange.add(sellOrder.getLimit().multiply(new BigDecimal(sellOrder.getQuantity())));
                    matchedSellOrders.put(sellOrder, sellOrder.getQuantity());
                    sellOrder.setQuantity(0);
                }
            }

            if (buyOrder.isAllOrNone() && buyOrder.getQuantity() > 0) {
                if (buyOrder.getStop() == null) return null;

                if (buyOrder.getType().equals(Type.LIMIT_ORDER)) buyOrder.setType(Type.STOP_LIMIT_ORDER);
                else buyOrder.setType(Type.STOP_ORDER);

                return null;
            }

            // future margin order check

            modifyUserBalance(buyOrder.getUserId(), totalValueChange.negate(), buyOrder.getRadnikId(), token);
            int totalQuantitySold = 0;

            for (Map.Entry<Order, Integer> entry : matchedSellOrders.entrySet()) {
                Order sellOrder = entry.getKey();
                Integer quantitySold = entry.getValue();

                modifyUserBalance(sellOrder.getUserId(), sellOrder.getLimit().multiply(new BigDecimal(quantitySold)), sellOrder.getRadnikId(), token);

                totalQuantitySold += quantitySold;
            }

            UserStockRequest userStockRequest = new UserStockRequest();
            userStockRequest.setUserId(buyOrder.getUserId());
            userStockRequest.setTicker(buyOrder.getTicker());
            userStockRequest.setQuantity(totalQuantitySold);
            userStockService.changeUserStockQuantity(userStockRequest);


            for (Order sellOrder : matchedSellOrders.keySet()) {
                if (sellOrder.getQuantity() == 0) orderRepository.delete(sellOrder);
                else orderRepository.save(sellOrder);
            }
            if (buyOrder.getQuantity() == 0) orderRepository.delete(buyOrder);
            else orderRepository.save(buyOrder);
        }

        return OrderMapper.toDto(buyOrder);
    }

    private OrderDto placeSellOrder(Order sellOrder, String token) {
        orderRepository.save(sellOrder);
        checkStopOrderAndStopLimitOrder(token);

        UserStockRequest userStockRequest = new UserStockRequest();
        userStockRequest.setUserId(sellOrder.getUserId());
        userStockRequest.setTicker(sellOrder.getTicker());
        userStockRequest.setQuantity(-sellOrder.getQuantity());
        boolean success = userStockService.changeUserStockQuantity(userStockRequest);
        if (!success) return null;

        if (sellOrder.getType().equals(Type.MARKET_ORDER) || sellOrder.getType().equals(Type.LIMIT_ORDER)) {
            List<Order> buyOrders = findAllBuyOrdersForTicker(sellOrder.getTicker());
            BigDecimal totalValueChange = BigDecimal.ZERO;
            Map<Order, Integer> matchedBuyOrders = new HashMap<>();

            for (Order buyOrder : buyOrders) {
                if (sellOrder.getQuantity() == 0 || (sellOrder.getType().equals(Type.LIMIT_ORDER) && sellOrder.getLimit().compareTo(buyOrder.getLimit()) >= 0)) break;

                if (buyOrder.getQuantity() > sellOrder.getQuantity()) {
                    buyOrder.setQuantity(buyOrder.getQuantity() - sellOrder.getQuantity());
                    totalValueChange = totalValueChange.add(buyOrder.getLimit().multiply(new BigDecimal(sellOrder.getQuantity())));
                    matchedBuyOrders.put(buyOrder, sellOrder.getQuantity());
                    sellOrder.setQuantity(0);
                } else {
                    sellOrder.setQuantity(sellOrder.getQuantity() - buyOrder.getQuantity());
                    totalValueChange = totalValueChange.add(buyOrder.getLimit().multiply(new BigDecimal(buyOrder.getQuantity())));
                    matchedBuyOrders.put(buyOrder, buyOrder.getQuantity());
                    buyOrder.setQuantity(0);
                }
            }

            if (sellOrder.isAllOrNone() && sellOrder.getQuantity() > 0) {
                if (sellOrder.getStop() == null) return null;

                if (sellOrder.getType().equals(Type.LIMIT_ORDER)) sellOrder.setType(Type.STOP_LIMIT_ORDER);
                else sellOrder.setType(Type.STOP_ORDER);

                return null;
            }

            // future margin order check

            modifyUserBalance(sellOrder.getUserId(), totalValueChange, sellOrder.getRadnikId(), token);

            for (Map.Entry<Order, Integer> entry : matchedBuyOrders.entrySet()) {
                Order buyOrder = entry.getKey();
                Integer quantityBought = entry.getValue();

                // Modify buyer's balance
                modifyUserBalance(buyOrder.getUserId(), buyOrder.getLimit().multiply(new BigDecimal(quantityBought)).negate(), buyOrder.getRadnikId(), token);

                // Modify buyer's stock quantity
                UserStockRequest userStockRequestBuyer = new UserStockRequest();
                userStockRequestBuyer.setUserId(buyOrder.getUserId());
                userStockRequestBuyer.setTicker(buyOrder.getTicker());
                userStockRequestBuyer.setQuantity(quantityBought);
                userStockService.changeUserStockQuantity(userStockRequestBuyer);
            }

            for (Order buyOrder : matchedBuyOrders.keySet()) {
                if (buyOrder.getQuantity() == 0) orderRepository.delete(buyOrder);
                else orderRepository.save(buyOrder);
            }
            if (sellOrder.getQuantity() == 0) orderRepository.delete(sellOrder);
            else orderRepository.save(sellOrder);
        }

        return OrderMapper.toDto(sellOrder);
    }

    @Override
    public BigDecimal approximateOrderValue(OrderRequest orderRequest) {
        Order buyOrder = OrderMapper.mapOrderRequestToOrder(orderRequest);
        List<Order> sellOrders = findAllSellOrdersForTicker(buyOrder.getTicker());
        BigDecimal approximateValue = BigDecimal.ZERO;
        int remainingQuantity = buyOrder.getQuantity();

        switch(buyOrder.getType()) {

            case Type.MARKET_ORDER -> {
                for (Order sellOrder : sellOrders) {
                    if (remainingQuantity == 0) break;

                    int quantityToUse = Math.min(remainingQuantity, sellOrder.getQuantity());
                    approximateValue = approximateValue.add(sellOrder.getLimit().multiply(new BigDecimal(quantityToUse)));
                    remainingQuantity -= quantityToUse;
                }
            }

            case Type.LIMIT_ORDER -> {
                for (Order sellOrder : sellOrders) {
                    if (remainingQuantity == 0 || sellOrder.getLimit().compareTo(buyOrder.getLimit()) >= 0) break;

                    int quantityToUse = Math.min(remainingQuantity, sellOrder.getQuantity());
                    approximateValue = approximateValue.add(sellOrder.getLimit().multiply(new BigDecimal(quantityToUse)));
                    remainingQuantity -= quantityToUse;
                }
                approximateValue = approximateValue.add(buyOrder.getLimit().multiply(new BigDecimal(remainingQuantity)));
            }

            case Type.STOP_ORDER, Type.STOP_LIMIT_ORDER -> approximateValue = approximateValue.add(buyOrder.getStop().multiply(new BigDecimal(remainingQuantity)).multiply(new BigDecimal("1.02")));

        }

        return approximateValue;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Override
    public List<OrderDto> getOrdersForUser(Long userId) {
        return orderRepository.findAllByUserId(userId);
    }

    private void checkStopOrderAndStopLimitOrder(String token) {
        List<Order> allOrders = orderRepository.findAll();
        for (Order order : allOrders) {
            if (order.getType().equals(Type.STOP_ORDER)) {
                if (order.getAction().equals(Action.BUY)) {
                    List<Order> sellOrders = findAllSellOrdersForTicker(order.getTicker());
                    if (!sellOrders.isEmpty() && sellOrders.get(0).getLimit().compareTo(order.getStop()) >= 0) {
                        order.setType(Type.MARKET_ORDER);
                        placeBuyOrder(order, token);
                    }
                } else if (order.getAction().equals(Action.SELL)) {
                    List<Order> buyOrders = findAllBuyOrdersForTicker(order.getTicker());
                    if (!buyOrders.isEmpty() && buyOrders.get(0).getLimit().compareTo(order.getStop()) <= 0) {
                        order.setType(Type.MARKET_ORDER);
                        placeSellOrder(order, token);
                    }
                }
            } else if (order.getType().equals(Type.STOP_LIMIT_ORDER)) {
                if (order.getAction().equals(Action.BUY)) {
                    List<Order> sellOrders = findAllSellOrdersForTicker(order.getTicker());
                    if (!sellOrders.isEmpty() && sellOrders.get(0).getLimit().compareTo(order.getStop()) >= 0) {
                        order.setType(Type.LIMIT_ORDER);
                        placeBuyOrder(order, token);
                    }
                } else if (order.getAction().equals(Action.SELL)) {
                    List<Order> buyOrders = findAllBuyOrdersForTicker(order.getTicker());
                    if (!buyOrders.isEmpty() && buyOrders.get(0).getLimit().compareTo(order.getStop()) <= 0) {
                        order.setType(Type.LIMIT_ORDER);
                        placeSellOrder(order, token);
                    }
                }
            }
        }
    }

    @Override
    public List<Order> findAllBuyOrdersForTicker(String ticker) {
        return orderRepository.findAllByActionAndTicker(Action.BUY, ticker)
                .stream()
                .sorted(Comparator.comparing(Order::getLimit).reversed())
                .toList();
    }

    @Override
    public List<Order> findAllSellOrdersForTicker(String ticker) {
        return orderRepository.findAllByActionAndTicker(Action.SELL, ticker)
                .stream()
                .sorted(Comparator.comparing(Order::getLimit))
                .toList();
    }

    private void modifyUserBalance(Long userId, BigDecimal valueChange, Long radnikId, String token) {

        String marzniRacunUpdateFundsEndpoint = "localhost:8082/api/marzniRacuni/updateBalance";
        Gson gson = new Gson();

        PairDTO pair = new PairDTO();
        pair.setValueChange(valueChange);
        pair.setUserId(userId);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest stocksRequest = HttpRequest.newBuilder()
                .uri(URI.create(marzniRacunUpdateFundsEndpoint))
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(pair)))
                .build();

        if (radnikId != null) {
            String getRadnikByIdEndpoint = "https://banka-4-dev.si.raf.edu.rs/user-service/api/radnik/profit/" + radnikId + "/" + valueChange;
            HttpRequest radnikRequest = HttpRequest.newBuilder()
                    .uri(URI.create(getRadnikByIdEndpoint))
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            try {
                client.send(radnikRequest, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                System.out.println("Failed to send balance update to MarniRacunController: " + e);
            }
        }

        try {
            client.send(stocksRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Failed to send balance update to MarniRacunController: " + e);
        }
    }

}
