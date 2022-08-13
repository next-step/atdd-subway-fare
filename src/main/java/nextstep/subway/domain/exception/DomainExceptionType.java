package nextstep.subway.domain.exception;

import org.springframework.http.HttpStatus;

public enum DomainExceptionType {

    STATIONS_NOT_CONNECTED("역 사이에 경로가 없습니다.", HttpStatus.NOT_FOUND),
    EQUAL_SOURCE_TARGET_STATION("출발역과 도착역이 동일합니다.", HttpStatus.BAD_REQUEST)
    ;

    private String message;
    private HttpStatus status;

    DomainExceptionType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
