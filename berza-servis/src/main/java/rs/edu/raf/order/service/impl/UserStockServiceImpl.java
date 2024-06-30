package rs.edu.raf.order.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import rs.edu.raf.order.dto.UserStockDto;
import rs.edu.raf.order.dto.UserStockRequest;
import rs.edu.raf.order.model.UserStock;
import rs.edu.raf.order.repository.UserStockRepository;
import rs.edu.raf.order.service.UserStockService;
import rs.edu.raf.order.service.mapper.UserStockMapper;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class UserStockServiceImpl implements UserStockService {

    private final UserStockRepository userStockRepository;

    @Override
    public boolean changeUserStockQuantity(UserStockRequest userStockRequest) {
        Long userId = userStockRequest.getUserId();
        String ticker = userStockRequest.getTicker();
        Integer quantity = userStockRequest.getQuantity();

        UserStock userStock = userStockRepository.findByUserIdAndTicker(userId, ticker);
        if (userStock == null) {
            if (quantity >= 0) {
                UserStock newUserStock = new UserStock();
                newUserStock.setUserId(userId);
                newUserStock.setTicker(ticker);
                newUserStock.setQuantity(quantity);
                newUserStock.setCurrentAsk(new BigDecimal("1.0"));
                newUserStock.setCurrentBid(new BigDecimal("1.0"));
                userStockRepository.save(newUserStock);
            } else {
                return false;
            }
        } else {
            int newQuantity = userStock.getQuantity() + quantity;
            if (newQuantity < 0) {
                return false;
            } else if (newQuantity == 0) {
                userStockRepository.deleteByUserIdAndTicker(userId, ticker);
            } else {
                userStock.setQuantity(newQuantity);
                userStockRepository.save(userStock);
            }
        }
        return true;
    }

    @Override
    public UserStockDto getUserStock(Long userId, String ticker) {
        return UserStockMapper.toDto(userStockRepository.findByUserIdAndTicker(userId, ticker));
    }

    @Override
    public List<UserStockDto> getUserStocks(Long userId) {
        return userStockRepository.findAllByUserId(userId)
                .stream()
                .map(UserStockMapper::toDto)
                .toList();
    }
}
