package nextstep.member.domain.exception;

import nextstep.common.exception.ErrorCode;

public class NotFoundMemberException extends MemberException {

    public NotFoundMemberException(ErrorCode errorCode) {
        super(errorCode);
    }
}
