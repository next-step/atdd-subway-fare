package nextstep.subway.domain.exception;

import org.springframework.http.HttpStatus;

public enum DomainExceptionType {

    STATIONS_NOT_CONNECTED("서로 연결되지 않은 역 입니다.", HttpStatus.BAD_REQUEST),
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
