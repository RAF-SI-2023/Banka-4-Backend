package rs.edu.raf.order.service;

import rs.edu.raf.order.dto.OrderDto;
import rs.edu.raf.order.dto.OrderRequest;
import rs.edu.raf.order.model.Enums.Action;
import rs.edu.raf.order.model.Order;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * This interface defines methods for managing orders.
 */
public interface OrderService {

    /**
     * Places an order based on the provided order request.
     *
     * @param orderRequest The order request containing order details.
     * @return An OrderDto object representing the placed order.
     */
    OrderDto placeOrder(OrderRequest orderRequest);

    /**
     * Approximates the value of an order based on the order request.
     *
     * @param order The order request to approximate.
     * @return The approximate value of the order.
     */
    BigDecimal approximateOrderValue(OrderRequest order);

    /**
     * Retrieves all orders.
     *
     * @return A list of OrderDto objects representing all orders.
     */
    List<OrderDto> getAllOrders();

    /**
     * Retrieves orders for a specific user.
     *
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return A list of OrderDto objects representing the orders for the specified user.
     */
    List<OrderDto> getOrdersForUser(Long userId);

    /**
     * Finds all buy orders for a given ticker symbol.
     *
     * @param ticker The ticker symbol for which to find buy orders.
     * @return A list of Order objects representing all buy orders for the specified ticker.
     */
    List<Order> findAllBuyOrdersForTicker(String ticker);

    /**
     * Finds all sell orders for a given ticker symbol.
     *
     * @param ticker The ticker symbol for which to find sell orders.
     * @return A list of Order objects representing all sell orders for the specified ticker.
     */
    List<Order> findAllSellOrdersForTicker(String ticker);

}
