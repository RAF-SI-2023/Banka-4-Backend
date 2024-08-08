package rs.edu.raf.order.service.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.order.dto.OrderDto;
import rs.edu.raf.order.dto.OrderRequest;
import rs.edu.raf.order.model.Enums.Status;
import rs.edu.raf.order.model.Enums.Type;
import rs.edu.raf.order.model.Order;

@Component
public class OrderMapper {

    public Order mapOrderRequestToOrder(OrderRequest orderRequest) {
        String type;

        if      (orderRequest.getLimit() == null && orderRequest.getStop() == null) type = Type.MARKET_ORDER;
        else if (orderRequest.getLimit() != null && orderRequest.getStop() == null) type = Type.LIMIT_ORDER;
        else if (orderRequest.getLimit() == null && orderRequest.getStop() != null) type = Type.STOP_ORDER;
        else                                                                        type = Type.STOP_LIMIT_ORDER;

        return Order.builder()
                .ticker(orderRequest.getTicker())
                .quantity(orderRequest.getQuantity())
                .limit(orderRequest.getLimit())
                .stop(orderRequest.getStop())
                .action(orderRequest.getAction())
                .type(type)
                .status(Status.PENDING)
                .lastModified(System.currentTimeMillis())
                .userId(orderRequest.getUserId())
                .build();
    }

    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .ticker(order.getTicker())
                .quantity(order.getQuantity())
                .limit(order.getLimit())
                .stop(order.getStop())
                .action(order.getAction())
                .status(order.getStatus())
                .build();
    }
}
