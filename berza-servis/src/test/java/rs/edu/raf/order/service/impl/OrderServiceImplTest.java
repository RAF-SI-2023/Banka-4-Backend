package rs.edu.raf.order.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.order.model.Enums.Action;
import rs.edu.raf.order.model.Enums.Type;
import rs.edu.raf.order.model.Order;
import rs.edu.raf.order.repository.OrderRepository;
import rs.edu.raf.order.service.UserStockService;
import rs.edu.raf.order.service.impl.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private UserStockService userStockService;

    @Mock
    private OrderRepository orderRepository;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void acceptOrderShouldUpdateStatusWhenOrderIsPending() {
        Long orderId = 1L;
        String token = "token";
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(1L);
        order.setRadnikId(1L);
        order.setTicker("AAPL");
        order.setQuantity(10);
        order.setLimit(new BigDecimal("150.00"));
        order.setStop(new BigDecimal("140.00"));
        order.setAllOrNone(false);
        order.setMargin(false);
        order.setAction(Action.BUY);
        order.setType(Type.MARKET_ORDER);
        order.setStatus("PENDING");
        order.setLastModified(System.currentTimeMillis());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(userStockService.changeUserStockQuantity(any())).thenReturn(true);

        assertNotNull(orderService.acceptOrder(orderId, token));
        assertEquals("ACCEPTED", order.getStatus());
    }

    @Test
    public void acceptOrderShouldReturnNullWhenOrderIsNotPending() {
        Long orderId = 1L;
        String token = "token";
        Order order = new Order();
        order.setStatus("ACCEPTED");
        order.setAction(Action.BUY);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        assertNull(orderService.acceptOrder(orderId, token));
    }

    @Test
    public void acceptOrderShouldReturnNullWhenOrderDoesNotExist() {
        Long orderId = 1L;
        String token = "token";

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertNull(orderService.acceptOrder(orderId, token));
    }
}