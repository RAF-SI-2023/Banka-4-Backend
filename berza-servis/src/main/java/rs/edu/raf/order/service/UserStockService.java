package rs.edu.raf.order.service;

import rs.edu.raf.order.dto.UserStockDto;
import rs.edu.raf.order.dto.UserStockRequest;

import java.util.List;

/**
 * This interface defines methods for managing user stocks.
 */
public interface UserStockService {

    /**
     * Changes the quantity of user stocks based on the provided request.
     *
     * @param userStockRequest The request containing the user ID, ticker symbol, and quantity change.
     * @return True if the user stock quantity was successfully changed, otherwise false.
     */
    boolean changeUserStockQuantity(UserStockRequest userStockRequest);
    UserStockDto setPublicQuantity(Long user_stock_id, Integer publicQuantity, Long userId);

    /**
     * Retrieves information about a specific user stock.
     *
     * @param userId The ID of the user whose stock information is to be retrieved.
     * @param ticker The ticker symbol of the stock.
     * @return A UserStockDto object representing the information about the user's stock.
     */
    UserStockDto getUserStock(Long userId, String ticker);

    /**
     * Retrieves all stocks owned by a specific user.
     *
     * @param userId The ID of the user whose stocks are to be retrieved.
     * @return A list of UserStockDto objects representing all stocks owned by the user.
     */
    List<UserStockDto> getUserStocks(Long userId);
}
