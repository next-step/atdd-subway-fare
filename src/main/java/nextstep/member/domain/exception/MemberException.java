package nextstep.member.domain.exception;

import lombok.Getter;
import nextstep.common.exception.ErrorCode;

@Getter
public class MemberException extends RuntimeException {

    private final ErrorCode errorCode;

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
