package rs.edu.raf.order.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.order.dto.OrderDto;
import rs.edu.raf.order.dto.OrderRequest;
import rs.edu.raf.order.model.Enums.Action;
import rs.edu.raf.order.model.Enums.Status;
import rs.edu.raf.order.model.Enums.Type;
import rs.edu.raf.order.model.Order;
import rs.edu.raf.order.repository.OrderRepository;
import rs.edu.raf.order.service.mapper.OrderMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Order order1 = Order.builder()
                .id(1L)
                .userId(1L)
                .ticker("AAPL")
                .quantity(10)
                .limit(new BigDecimal("150.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.BUY)
                .type(Type.LIMIT_ORDER)
                .status(Status.APPROVED)
                .lastModified(System.currentTimeMillis())
                .build();

        Order order2 = Order.builder()
                .id(2L)
                .userId(2L)
                .ticker("AAPL")
                .quantity(15)
                .limit(new BigDecimal("160.00"))
                .stop(new BigDecimal("150.00"))
                .allOrNone(true)
                .margin(true)
                .action(Action.SELL)
                .type(Type.STOP_LIMIT_ORDER)
                .status(Status.REJECTED)
                .lastModified(System.currentTimeMillis())
                .build();

        Order order3 = Order.builder()
                .id(3L)
                .userId(1L)
                .ticker("AAPL")
                .quantity(20)
                .limit(new BigDecimal("170.00"))
                .stop(new BigDecimal("160.00"))
                .allOrNone(false)
                .margin(true)
                .action(Action.BUY)
                .type(Type.STOP_LIMIT_ORDER)
                .status(Status.PENDING)
                .lastModified(System.currentTimeMillis())
                .build();

        Order order4 = Order.builder()
                .id(4L)
                .userId(2L)
                .ticker("AAPL")
                .quantity(25)
                .limit(new BigDecimal("145.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.SELL)
                .type(Type.LIMIT_ORDER)
                .status(Status.APPROVED)
                .lastModified(System.currentTimeMillis())
                .build();

        List<Order> allOrders = Arrays.asList(order1, order2, order3, order4);

        OrderDto order1Dto = OrderDto.builder()
                .id(1L)
                .userId(1L)
                .ticker("AAPL")
                .quantity(10)
                .limit(new BigDecimal("150.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.BUY)
                .type(Type.LIMIT_ORDER)
                .status(Status.APPROVED)
                .build();

        OrderDto order2Dto = OrderDto.builder()
                .id(2L)
                .userId(2L)
                .ticker("AAPL")
                .quantity(15)
                .limit(new BigDecimal("160.00"))
                .stop(new BigDecimal("150.00"))
                .allOrNone(true)
                .margin(true)
                .action(Action.SELL)
                .type(Type.STOP_LIMIT_ORDER)
                .status(Status.REJECTED)
                .build();

        OrderDto order3Dto = OrderDto.builder()
                .id(3L)
                .userId(1L)
                .ticker("AAPL")
                .quantity(20)
                .limit(new BigDecimal("170.00"))
                .stop(new BigDecimal("160.00"))
                .allOrNone(false)
                .margin(true)
                .action(Action.BUY)
                .type(Type.STOP_LIMIT_ORDER)
                .status(Status.PENDING)
                .build();

        OrderDto order4Dto = OrderDto.builder()
                .id(4L)
                .userId(2L)
                .ticker("AAPL")
                .quantity(25)
                .limit(new BigDecimal("145.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.SELL)
                .type(Type.LIMIT_ORDER)
                .status(Status.APPROVED)
                .build();

        when(orderRepository.findAll()).thenReturn(allOrders);
        when(orderRepository.findAllByUserId(1L)).thenReturn(Arrays.asList(order1Dto, order3Dto));
        when(orderRepository.findAllByUserId(2L)).thenReturn(Arrays.asList(order2Dto, order4Dto));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(orderRepository.findById(2L)).thenReturn(Optional.of(order2));
        when(orderRepository.findById(3L)).thenReturn(Optional.of(order3));
        when(orderRepository.findById(4L)).thenReturn(Optional.of(order4));
        when(orderMapper.toDto(order1)).thenReturn(OrderDto.builder()
                .id(1L)
                .userId(1L)
                .ticker("AAPL")
                .quantity(10)
                .limit(new BigDecimal("150.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.BUY)
                .type(Type.LIMIT_ORDER)
                .status(Status.APPROVED)
                .build());

        when(orderMapper.toDto(order2)).thenReturn(OrderDto.builder()
                .id(2L)
                .userId(2L)
                .ticker("AAPL")
                .quantity(15)
                .limit(new BigDecimal("160.00"))
                .stop(new BigDecimal("150.00"))
                .allOrNone(true)
                .margin(true)
                .action(Action.SELL)
                .type(Type.STOP_LIMIT_ORDER)
                .status(Status.REJECTED)
                .build());

        when(orderMapper.toDto(order3)).thenReturn(OrderDto.builder()
                .id(3L)
                .userId(1L)
                .ticker("AAPL")
                .quantity(20)
                .limit(new BigDecimal("170.00"))
                .stop(new BigDecimal("160.00"))
                .allOrNone(false)
                .margin(true)
                .action(Action.BUY)
                .type(Type.STOP_LIMIT_ORDER)
                .status(Status.PENDING)
                .build());

        when(orderMapper.toDto(order4)).thenReturn(OrderDto.builder()
                .id(4L)
                .userId(2L)
                .ticker("AAPL")
                .quantity(25)
                .limit(new BigDecimal("145.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.SELL)
                .type(Type.LIMIT_ORDER)
                .status(Status.APPROVED)
                .build());
    }

    @Test
    public void testPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest();
        // Fill out the orderRequest fields as necessary

        Order order = Order.builder()
                .id(5L)
                .userId(1L)
                .ticker("GOOGL")
                .quantity(30)
                .limit(new BigDecimal("2500.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.BUY)
                .type(Type.LIMIT_ORDER)
                .status(Status.PENDING)
                .lastModified(System.currentTimeMillis())
                .build();

        OrderDto orderDto = OrderDto.builder()
                .id(5L)
                .userId(1L)
                .ticker("GOOGL")
                .quantity(30)
                .limit(new BigDecimal("2500.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.BUY)
                .type(Type.LIMIT_ORDER)
                .status(Status.PENDING)
                .build();

        when(orderMapper.mapOrderRequestToOrder(orderRequest)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.placeOrder(orderRequest);

        verify(orderRepository, times(1)).save(order);
        assertEquals(orderDto, result);

    }

    @Test
    public void testGetAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();

        assertNotNull(orders);
        assertEquals(4, orders.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void testGetOrdersForUser() {
        Long userId = 1L;
        List<OrderDto> orders = orderService.getOrdersForUser(userId);

        assertNotNull(orders);
        assertEquals(2, orders.size());
        verify(orderRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    public void testFindAllBuyOrdersForTicker() {
        String ticker = "AAPL";

        Order order1 = Order.builder()
                .id(1L)
                .userId(1L)
                .ticker("AAPL")
                .quantity(10)
                .limit(new BigDecimal("150.00"))
                .stop(null)
                .allOrNone(false)
                .margin(false)
                .action(Action.BUY)
                .type(Type.LIMIT_ORDER)
                .status(Status.APPROVED)
                .lastModified(System.currentTimeMillis())
                .build();

        when(orderRepository.findAllByActionAndTicker(any(), any())).thenReturn(List.of(order1));

        List<Order> result = orderService.findAllBuyOrdersForTicker(ticker);

        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals(new BigDecimal("170.00"), result.get(0).getLimit()); // order3 has the highest limit
//        assertEquals(new BigDecimal("150.00"), result.get(1).getLimit()); // order1 has the second highest limit
//        assertTrue(result.stream().allMatch(order -> order.getStatus().equals(Status.APPROVED)));

        verify(orderRepository, times(1)).findAllByActionAndTicker(Action.BUY, ticker);
    }

}
