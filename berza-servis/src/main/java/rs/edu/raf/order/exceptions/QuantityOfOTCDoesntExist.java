package rs.edu.raf.order.exceptions;

import org.springframework.http.HttpStatus;
import rs.edu.raf.stocks.exceptions.CustomException;
import rs.edu.raf.stocks.exceptions.ErrorCode;

public class QuantityOfOTCDoesntExist extends CustomException {

    public QuantityOfOTCDoesntExist() {
        super("Quantity requested of otc doesn't exist!", ErrorCode.AMOUNT_OF_OTC_DOESNT_EXIST, HttpStatus.BAD_REQUEST);
    }
}
