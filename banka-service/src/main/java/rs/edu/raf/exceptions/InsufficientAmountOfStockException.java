package rs.edu.raf.exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientAmountOfStockException extends CustomException{
    public InsufficientAmountOfStockException(String message) {
        super(message, ErrorCode.INSUFFICIENT_AMOUNT_OF_STOCK, HttpStatus.BAD_REQUEST);
    }
}
