package nextstep.subway.section.exception;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.common.exception.ErrorCode;

public class DoesNotLongerThanExistingSectionException extends BusinessException {
    public DoesNotLongerThanExistingSectionException() {
        super(ErrorCode.DOES_NOT_LONGER_THAN_EXISTING_SECTION);
    }
}
