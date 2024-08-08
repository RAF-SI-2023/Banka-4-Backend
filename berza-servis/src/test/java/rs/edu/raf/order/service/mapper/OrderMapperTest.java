package rs.edu.raf.order.service.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.order.dto.OrderDto;
import org.junit.jupiter.api.Test;
import rs.edu.raf.order.dto.OrderRequest;
import rs.edu.raf.order.model.Order;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class OrderMapperTest {

    @Mock
    private OrderMapper orderMapper;

    @BeforeEach
    public void setup() {
        orderMapper = new OrderMapper();
    }

    @Test
    public void testMapOrderRequestToOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setTicker("AAPL");
        orderRequest.setQuantity(10);
        orderRequest.setLimit(BigDecimal.valueOf(100.0));
        orderRequest.setStop(BigDecimal.valueOf(90.0));
        orderRequest.setAction("BUY");

        Order order = orderMapper.mapOrderRequestToOrder(orderRequest);

        assertNotNull(order);
        assertEquals("AAPL", order.getTicker());
        assertEquals(10, order.getQuantity());
        assertEquals(BigDecimal.valueOf(100.0), order.getLimit());
        assertEquals(BigDecimal.valueOf(90.0), order.getStop());
        assertEquals("BUY", order.getAction());
    }

    @Test
    public void testToDto() {
        Order order = new Order();
        order.setTicker("AAPL");
        order.setQuantity(10);
        order.setLimit(BigDecimal.valueOf(100.0));
        order.setStop(BigDecimal.valueOf(90.0));
        order.setAction("BUY");

        OrderDto orderDto = orderMapper.toDto(order);

        assertNotNull(orderDto);
        assertEquals("AAPL", orderDto.getTicker());
        assertEquals(10, orderDto.getQuantity());
        assertEquals(BigDecimal.valueOf(100.0), orderDto.getLimit());
        assertEquals(BigDecimal.valueOf(90.0), orderDto.getStop());
        assertEquals("BUY", orderDto.getAction());
    }
}