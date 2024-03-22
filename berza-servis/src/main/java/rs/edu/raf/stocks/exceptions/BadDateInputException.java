package rs.edu.raf.stocks.exceptions;

import org.springframework.http.HttpStatus;

public class BadDateInputException extends CustomException{

    public BadDateInputException() {
        super("Los format datuma! Mora biti (yyyy-mm-dd)", ErrorCode.BAD_DATE_INPUT, HttpStatus.BAD_REQUEST);
    }
}
