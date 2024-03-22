package rs.edu.raf.stocks.exceptions;

import org.springframework.http.HttpStatus;

public class TickerDoesntExist extends CustomException{

    public TickerDoesntExist() {
        super("Kompanija sa prosledjenim tickerom ne postoji!", ErrorCode.TICKER_DOESNT_EXIST, HttpStatus.BAD_REQUEST);    }
}
