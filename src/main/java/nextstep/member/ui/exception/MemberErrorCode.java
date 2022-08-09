package nextstep.member.ui.exception;

import nextstep.common.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    MemberErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public int status() {
        return httpStatus.value();
    }

    @Override
    public String code() {
        return name();
    }
}
