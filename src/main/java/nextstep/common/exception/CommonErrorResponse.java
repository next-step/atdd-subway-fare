package nextstep.common.exception;

import lombok.Getter;

@Getter
public class CommonErrorResponse {

    private final String message;

    private final String code;

    private CommonErrorResponse(ErrorCode errorCode) {
        this.message = errorCode.getMessage();
        this.code = errorCode.getCode();
    }

    public static CommonErrorResponse of(ErrorCode errorCode) {
        return new CommonErrorResponse(errorCode);
    }
}
