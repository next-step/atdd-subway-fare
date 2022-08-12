package nextstep.common;

import org.springframework.http.HttpStatus;

public enum GlobalErrorCode implements ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN)
    ;

    private final HttpStatus httpStatus;

    GlobalErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public int status() {
        return httpStatus.value();
    }

    @Override
    public String code() {
        return name();
    }
}
