package nextstep.member.domain.exception;

import nextstep.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotFoundMemberException extends MemberException {

    public NotFoundMemberException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.NOT_FOUND);
    }
}
