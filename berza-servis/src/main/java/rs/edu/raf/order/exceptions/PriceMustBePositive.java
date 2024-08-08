package rs.edu.raf.order.exceptions;

import org.springframework.http.HttpStatus;
import rs.edu.raf.stocks.exceptions.CustomException;
import rs.edu.raf.stocks.exceptions.ErrorCode;

public class PriceMustBePositive extends CustomException {
    public PriceMustBePositive() {
        super("Price must be positive and not equals 0!", ErrorCode.PRICE_MUST_BE_POSITIVE, HttpStatus.BAD_REQUEST);
    }
}
