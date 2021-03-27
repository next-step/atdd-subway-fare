package nextstep.subway.exceptions;

public class InvalidLineCostException extends RuntimeException {
    public static final String DEFAULT_MSG = "지하철 노선의 추가요금이 유효하지 않습니다.";

    public InvalidLineCostException() {
        super(DEFAULT_MSG);
    }

    public InvalidLineCostException(String message) {
        super(message);
    }
}
