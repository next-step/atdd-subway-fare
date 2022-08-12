package nextstep.station.ui.exception;

import nextstep.common.ErrorCode;
import org.springframework.http.HttpStatus;

public enum StationErrorCode implements ErrorCode {
    CANT_DELETE_STATION(HttpStatus.BAD_REQUEST)
    ;

    private final HttpStatus httpStatus;

    StationErrorCode(HttpStatus httpStatus) {
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
