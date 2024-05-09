package rs.edu.raf.exceptions;

import org.springframework.http.HttpStatus;

public class WrongEmployeeException extends CustomException{
    public WrongEmployeeException(String message) {
        super(message, ErrorCode.WRONG_EMPLOYEE, HttpStatus.BAD_REQUEST);
    }
}
