package atdd.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FailedLoginException extends RuntimeException {
    public FailedLoginException(String message) {
        super(message);
    }
}
