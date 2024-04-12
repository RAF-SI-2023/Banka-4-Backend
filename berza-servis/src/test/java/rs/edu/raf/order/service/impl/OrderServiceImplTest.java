package rs.edu.raf.order.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.order.dto.OrderDto;
import rs.edu.raf.order.dto.OrderRequest;
import rs.edu.raf.order.dto.UserStockRequest;
import rs.edu.raf.order.model.Enums.Action;
import rs.edu.raf.order.model.Enums.Type;
import rs.edu.raf.order.model.Order;
import rs.edu.raf.order.repository.OrderRepository;
import rs.edu.raf.order.service.UserStockService;
import rs.edu.raf.order.service.mapper.OrderMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserStockService userStockService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));
        orderRequest.setStop(BigDecimal.valueOf(90.0));

        Order order = OrderMapper.mapOrderRequestToOrder(orderRequest);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertNotNull(orderService.placeOrder(orderRequest));
    }

    @Test
    public void testApproximateOrderValue() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));
        orderRequest.setStop(BigDecimal.valueOf(90.0));

        assertNotNull(orderService.approximateOrderValue(orderRequest));
    }

    @Test
    public void testGetAllOrders() {
        Order order1 = OrderMapper.mapOrderRequestToOrder(new OrderRequest());
        Order order2 = OrderMapper.mapOrderRequestToOrder(new OrderRequest());

        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        List<OrderDto> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    public void testGetOrdersForUser() {
        Long userId = 1L;

        Order order1 = OrderMapper.mapOrderRequestToOrder(new OrderRequest());
        order1.setUserId(userId);

        Order order2 = OrderMapper.mapOrderRequestToOrder(new OrderRequest());
        order2.setUserId(userId);

        when(orderRepository.findAllByUserId(userId)).thenReturn(
                Stream.of(order1, order2)
                        .map(OrderMapper::toDto)
                        .toList()
        );

        List<OrderDto> orders = orderService.getOrdersForUser(userId);

        assertNotNull(orders);
        assertEquals(2, orders.size());
    }

    @Test
    public void testPlaceSellOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.SELL);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));
        orderRequest.setStop(BigDecimal.valueOf(90.0));

        Order order = OrderMapper.mapOrderRequestToOrder(orderRequest);

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userStockService.changeUserStockQuantity(any(UserStockRequest.class))).thenReturn(true);

        assertNotNull(orderService.placeOrder(orderRequest));
    }

    @Test
    public void testPlaceAllOrNoneOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));
        orderRequest.setStop(BigDecimal.valueOf(90.0));
        orderRequest.setAllOrNone(true);

        Order order = OrderMapper.mapOrderRequestToOrder(orderRequest);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertNotNull(orderService.placeOrder(orderRequest));
    }

    @Test
    public void testPlaceOrderWithStopPrice() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));
        orderRequest.setStop(BigDecimal.valueOf(90.0));

        Order order = OrderMapper.mapOrderRequestToOrder(orderRequest);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertNotNull(orderService.placeOrder(orderRequest));
    }

    @Test
    public void testApproximateOrderValueWithMarketOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);

        assertNotNull(orderService.approximateOrderValue(orderRequest));
    }

    @Test
    public void testApproximateOrderValueWithLimitOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));

        assertNotNull(orderService.approximateOrderValue(orderRequest));
    }

    @Test
    public void testApproximateOrderValueWithStopOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setStop(BigDecimal.valueOf(90.0));

        assertNotNull(orderService.approximateOrderValue(orderRequest));
    }

    @Test
    public void testApproximateOrderValueWithStopLimitOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAction(Action.BUY);
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));
        orderRequest.setStop(BigDecimal.valueOf(90.0));

        assertNotNull(orderService.approximateOrderValue(orderRequest));
    }
}