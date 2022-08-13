package nextstep.subway.domain.exception;

import org.springframework.http.HttpStatus;

public class DomainException extends RuntimeException {
    private HttpStatus status;

    public DomainException(DomainExceptionType type) {
        super(type.getMessage());
        this.status = type.getStatus();
    }

    public HttpStatus getStatus() {
        return status;
    }
}
