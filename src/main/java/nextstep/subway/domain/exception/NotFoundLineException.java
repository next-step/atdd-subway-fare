package nextstep.subway.domain.exception;

import nextstep.common.exception.ErrorCode;

public class NotFoundLineException extends SubwayException {

    public NotFoundLineException(ErrorCode errorCode) {
        super(errorCode);
    }
}
