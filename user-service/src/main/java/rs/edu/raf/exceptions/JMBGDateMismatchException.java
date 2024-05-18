package rs.edu.raf.exceptions;

import org.springframework.http.HttpStatus;

public class JMBGDateMismatchException extends CustomException{

        public JMBGDateMismatchException(String message) {
            super(message, ErrorCode.JMBG_AND_DATE_MISMATCH, HttpStatus.BAD_REQUEST);
        }
}
