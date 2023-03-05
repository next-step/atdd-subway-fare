package nextstep.subway.domain.exception;

import lombok.Getter;
import nextstep.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class SubwayException extends RuntimeException {

    private final ErrorCode errorCode;

    private final HttpStatus httpStatus;

    public SubwayException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
