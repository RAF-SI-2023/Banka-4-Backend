package rs.edu.raf.order.exceptions;

import org.springframework.http.HttpStatus;
import rs.edu.raf.stocks.exceptions.CustomException;
import rs.edu.raf.stocks.exceptions.ErrorCode;

public class OTCOfferDoesntExist extends CustomException {

    public OTCOfferDoesntExist() {
        super("OTC offer doesn't exist", ErrorCode.OTC_OFFER_DOESNT_EXIST, HttpStatus.BAD_REQUEST);
    }
}
