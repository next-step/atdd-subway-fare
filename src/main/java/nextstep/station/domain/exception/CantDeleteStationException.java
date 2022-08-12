package nextstep.station.domain.exception;

public class CantDeleteStationException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "노선에 포함된 역을 삭제할 수 없습니다";

    public CantDeleteStationException() {
        super(DEFAULT_MESSAGE);
    }
}
