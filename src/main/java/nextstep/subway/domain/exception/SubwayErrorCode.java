package nextstep.subway.domain.exception;

import nextstep.common.exception.ErrorCode;

public enum SubwayErrorCode implements ErrorCode {

    NOT_FOUND_LINE("찾는 노선이 없습니다.");

    private final String message;

    SubwayErrorCode(String message) {
        this.message = message;
    }


    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getCode() {
        return this.name();
    }
}
