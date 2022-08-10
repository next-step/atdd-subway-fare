package nextstep.member.ui.exception;

import nextstep.common.ErrorCode;
import org.springframework.http.HttpStatus;

public enum FavoriteErrorCode implements ErrorCode {
    DUPLICATE_FAVORITE(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    FavoriteErrorCode(HttpStatus httpStatus) {
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
