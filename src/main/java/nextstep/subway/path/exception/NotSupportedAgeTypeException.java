package nextstep.subway.path.exception;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.common.exception.ErrorCode;

public class NotSupportedAgeTypeException extends BusinessException {
    public NotSupportedAgeTypeException() {
        super(ErrorCode.NOT_SUPPORTED_AGE_TYPE);
    }
}
