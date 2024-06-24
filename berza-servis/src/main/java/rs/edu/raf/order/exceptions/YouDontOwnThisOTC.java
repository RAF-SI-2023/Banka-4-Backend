package rs.edu.raf.order.exceptions;

import org.springframework.http.HttpStatus;
import rs.edu.raf.stocks.exceptions.CustomException;
import rs.edu.raf.stocks.exceptions.ErrorCode;

public class YouDontOwnThisOTC extends CustomException {

    public YouDontOwnThisOTC() {
        super("You don't own this otc offer and cannot accept or decline it!", ErrorCode.YOU_DONT_OWN_THIS_OTC, HttpStatus.BAD_REQUEST);
    }
}
