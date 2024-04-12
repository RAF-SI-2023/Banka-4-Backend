package rs.edu.raf.order.service.mapper;

import org.junit.jupiter.api.Test;
import rs.edu.raf.order.dto.UserStockDto;
import rs.edu.raf.order.model.UserStock;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UserStockMapperTest {

    @Test
    public void testToDto() {
        UserStock userStock = new UserStock();
        userStock.setTicker("AAPL");
        userStock.setQuantity(10);
        userStock.setCurrentBid(BigDecimal.valueOf(100.0));
        userStock.setCurrentAsk(BigDecimal.valueOf(90.0));

        UserStockDto userStockDto = UserStockMapper.toDto(userStock);

        assertNotNull(userStockDto);
        assertEquals("AAPL", userStockDto.getTicker());
        assertEquals(10, userStockDto.getQuantity());
        assertEquals(BigDecimal.valueOf(100.0), userStockDto.getCurrentBid());
        assertEquals(BigDecimal.valueOf(90.0), userStockDto.getCurrentAsk());
    }

    @Test
    public void testToDto_WhenUserStockIsNull() {
        UserStockDto userStockDto = UserStockMapper.toDto(null);

        assertNull(userStockDto);
    }
}