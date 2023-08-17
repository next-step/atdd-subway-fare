package nextstep.subway.path.exception;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.common.exception.ErrorCode;

public class NotSupportedDistanceFarePolicy extends BusinessException {
    public NotSupportedDistanceFarePolicy() {
        super(ErrorCode.NOT_SUPPORTED_FARE_POLICY);
    }
}
