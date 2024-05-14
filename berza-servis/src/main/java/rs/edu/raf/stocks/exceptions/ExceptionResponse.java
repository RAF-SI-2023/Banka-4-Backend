package rs.edu.raf.stocks.exceptions;

import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonCreator;
import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExceptionResponse {

    private String message;

    @JsonCreator
    public ExceptionResponse(@JsonProperty("message") String message) {
        this.message = message;
    }

    public ExceptionResponse(){}
}
