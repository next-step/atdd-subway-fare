package nextstep.subway.domain.exception;

import nextstep.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundLineException extends SubwayException {

    public NotFoundLineException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }
}
