package rs.edu.raf.order.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rs.edu.raf.order.dto.UserStockRequest;
import rs.edu.raf.order.model.UserStock;
import rs.edu.raf.order.repository.UserStockRepository;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserStockServiceImplTest {

    @InjectMocks
    private UserStockServiceImpl userStockService;

    @Mock
    private UserStockRepository userStockRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangeUserStockQuantity() {
        UserStockRequest userStockRequest = new UserStockRequest();
        userStockRequest.setUserId(1L);
        userStockRequest.setTicker("AAPL");
        userStockRequest.setQuantity(10);

        UserStock userStock = new UserStock();
        userStock.setUserId(1L);
        userStock.setTicker("AAPL");
        userStock.setQuantity(5);

        when(userStockRepository.findByUserIdAndTicker(1L, "AAPL")).thenReturn(userStock);

        boolean result = userStockService.changeUserStockQuantity(userStockRequest);

        assertTrue(result);
        verify(userStockRepository, times(1)).save(any(UserStock.class));
    }

    @Test
    public void testGetUserStock() {
        UserStock userStock = new UserStock();
        userStock.setUserId(1L);
        userStock.setTicker("AAPL");
        userStock.setQuantity(5);

        when(userStockRepository.findByUserIdAndTicker(1L, "AAPL")).thenReturn(userStock);

        assertNotNull(userStockService.getUserStock(1L, "AAPL"));
    }

    @Test
    public void testGetUserStocks() {
        assertNotNull(userStockService.getUserStocks(1L));
    }

    @Test
    public void testChangeUserStockQuantity_WhenUserStockIsNullAndQuantityIsNegative() {
        UserStockRequest userStockRequest = new UserStockRequest();
        userStockRequest.setUserId(1L);
        userStockRequest.setTicker("AAPL");
        userStockRequest.setQuantity(-10);

        when(userStockRepository.findByUserIdAndTicker(1L, "AAPL")).thenReturn(null);

        boolean result = userStockService.changeUserStockQuantity(userStockRequest);

        assertFalse(result);
        verify(userStockRepository, times(0)).save(any(UserStock.class));
    }

    @Test
    public void testChangeUserStockQuantity_WhenNewQuantityIsNegative() {
        UserStockRequest userStockRequest = new UserStockRequest();
        userStockRequest.setUserId(1L);
        userStockRequest.setTicker("AAPL");
        userStockRequest.setQuantity(-10);

        UserStock userStock = new UserStock();
        userStock.setUserId(1L);
        userStock.setTicker("AAPL");
        userStock.setQuantity(5);

        when(userStockRepository.findByUserIdAndTicker(1L, "AAPL")).thenReturn(userStock);

        boolean result = userStockService.changeUserStockQuantity(userStockRequest);

        assertFalse(result);
        verify(userStockRepository, times(0)).save(any(UserStock.class));
    }

    @Test
    public void testChangeUserStockQuantity_WhenNewQuantityIsZero() {
        UserStockRequest userStockRequest = new UserStockRequest();
        userStockRequest.setUserId(1L);
        userStockRequest.setTicker("AAPL");
        userStockRequest.setQuantity(-5);

        UserStock userStock = new UserStock();
        userStock.setUserId(1L);
        userStock.setTicker("AAPL");
        userStock.setQuantity(5);

        when(userStockRepository.findByUserIdAndTicker(1L, "AAPL")).thenReturn(userStock);

        boolean result = userStockService.changeUserStockQuantity(userStockRequest);

        assertTrue(result);
        verify(userStockRepository, times(1)).deleteByUserIdAndTicker(1L, "AAPL");
    }

    @Test
    public void testGetUserStock_WhenUserStockIsNull() {
        when(userStockRepository.findByUserIdAndTicker(1L, "AAPL")).thenReturn(null);

        assertNull(userStockService.getUserStock(1L, "AAPL"));
    }

    @Test
    public void testGetUserStocks_WhenUserHasNoStocks() {
        when(userStockRepository.findAllByUserId(1L)).thenReturn(new ArrayList<>());

        assertTrue(userStockService.getUserStocks(1L).isEmpty());
    }
}