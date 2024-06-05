package rs.edu.raf.exceptions;

import org.springframework.http.HttpStatus;

public class StockForUsersMarginAccountException extends CustomException{
    public StockForUsersMarginAccountException(String message) {
        super(message, ErrorCode.STOCK_FOR_USERS_MARGIN_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
