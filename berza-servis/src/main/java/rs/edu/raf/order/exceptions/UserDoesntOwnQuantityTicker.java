package rs.edu.raf.order.exceptions;

import org.springframework.http.HttpStatus;
import rs.edu.raf.stocks.exceptions.CustomException;
import rs.edu.raf.stocks.exceptions.ErrorCode;

public class UserDoesntOwnQuantityTicker extends CustomException {
    public UserDoesntOwnQuantityTicker() {
        super("User doesn't own specified quantity of ticker!", ErrorCode.USER_DOESNT_OWN_QUANTITY_TICKER, HttpStatus.BAD_REQUEST);
    }
}
