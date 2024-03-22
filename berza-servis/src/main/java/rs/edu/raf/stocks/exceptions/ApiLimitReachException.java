package rs.edu.raf.stocks.exceptions;

import org.springframework.http.HttpStatus;

public class ApiLimitReachException extends CustomException{

    public ApiLimitReachException() {
        super("Limit broja api poziva prevazidjen!", ErrorCode.API_LIMIT_REACHED, HttpStatus.BAD_REQUEST);
    }
}
