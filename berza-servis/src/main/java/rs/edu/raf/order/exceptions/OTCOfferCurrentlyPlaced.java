package rs.edu.raf.order.exceptions;

import org.springframework.http.HttpStatus;
import rs.edu.raf.stocks.exceptions.CustomException;
import rs.edu.raf.stocks.exceptions.ErrorCode;

public class OTCOfferCurrentlyPlaced extends CustomException {

    public OTCOfferCurrentlyPlaced() {
        super("Someone already placed a offer for this otc stock, if it gets declined you can place yours.", ErrorCode.OTC_OFFER_CURRENTLY_PLACED, HttpStatus.BAD_REQUEST);
    }
}
