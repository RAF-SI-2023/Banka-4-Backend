package rs.edu.raf.exceptions;

import org.springframework.http.HttpStatus;

public class BankAccountNotFoundException extends CustomException{

    public BankAccountNotFoundException(String message) {
        super(message,ErrorCode.BANK_ACCOUNT_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
