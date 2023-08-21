package nextstep.member.exception;

import nextstep.subway.common.exception.BusinessException;
import nextstep.subway.common.exception.ErrorCode;

public class NotSupportedRoleTypeException extends BusinessException {
    public NotSupportedRoleTypeException() {
        super(ErrorCode.NOT_SUPPORTED_ROLE_TYPE);
    }
}
