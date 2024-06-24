package rs.edu.raf.order.exceptions;

import org.springframework.http.HttpStatus;
import rs.edu.raf.stocks.exceptions.CustomException;
import rs.edu.raf.stocks.exceptions.ErrorCode;

public class UserDoesntOwnTicker extends CustomException {

    public UserDoesntOwnTicker() {
        super("User doesn't own specified ticker!", ErrorCode.USER_DOESNT_OWN_TICKER, HttpStatus.BAD_REQUEST);
    }
}
