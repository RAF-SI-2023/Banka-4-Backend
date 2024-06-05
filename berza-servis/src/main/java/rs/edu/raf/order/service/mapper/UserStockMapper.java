package rs.edu.raf.order.service.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.order.dto.UserStockDto;
import rs.edu.raf.order.model.UserStock;

@Component
public class UserStockMapper {

    public static UserStockDto toDto(UserStock userStock) {
        if (userStock == null) return null;

        UserStockDto userStockDto = new UserStockDto();
        userStockDto.setTicker(userStock.getTicker());
        userStockDto.setQuantity(userStock.getQuantity());
        userStockDto.setCurrentBid(userStock.getCurrentBid());
        userStockDto.setCurrentAsk(userStock.getCurrentAsk());
        return userStockDto;
    }

}
