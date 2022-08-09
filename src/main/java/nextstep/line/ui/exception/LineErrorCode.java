package nextstep.line.ui.exception;

import nextstep.common.ErrorCode;
import org.springframework.http.HttpStatus;

public enum LineErrorCode implements ErrorCode {
    ILLEGAL_SECTION_OPERATION(HttpStatus.BAD_REQUEST)
    ;

    private final HttpStatus httpStatus;

    LineErrorCode(HttpStatus httpStatus) {
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
