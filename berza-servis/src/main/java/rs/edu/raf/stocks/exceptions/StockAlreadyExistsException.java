package rs.edu.raf.stocks.exceptions;

import org.springframework.http.HttpStatus;

public class StockAlreadyExistsException extends CustomException{

        public StockAlreadyExistsException() {
            super("Akcija(stock) vec postoji!", ErrorCode.STOCK_ALREADY_EXISTS, HttpStatus.BAD_REQUEST);
        }
}
