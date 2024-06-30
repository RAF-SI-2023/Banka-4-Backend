package rs.edu.raf.order.service.mapper;

import org.springframework.stereotype.Component;
import rs.edu.raf.order.dto.PublicUserStockDto;
import rs.edu.raf.order.dto.UserStockDto;
import rs.edu.raf.order.model.UserStock;

@Component
public class PublicUserStockMapper {

    public PublicUserStockDto userStockToPublicUserStockDto(UserStock userStock) {
        PublicUserStockDto publicUserStockDto = new PublicUserStockDto();
        publicUserStockDto.setStockId(userStock.getId());
        publicUserStockDto.setTicker(userStock.getTicker());
        publicUserStockDto.setQuantity(userStock.getPublicQuantity());
        return publicUserStockDto;
    }
}
