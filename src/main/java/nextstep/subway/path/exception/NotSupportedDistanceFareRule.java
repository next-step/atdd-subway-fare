package nextstep.subway.path.exception;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.common.exception.ErrorCode;

public class NotSupportedDistanceFareRule extends BusinessException {
    public NotSupportedDistanceFareRule() {
        super(ErrorCode.NOT_SUPPORTED_FARE_RULE);
    }
}
