package nextstep.subway.exceptions;

public class InvalidCostException extends RuntimeException {
    public static final String DEFAULT_MSG = "운임 요금은 0원 이상이어야 합니다.";

    public InvalidCostException() {
        super(DEFAULT_MSG);
    }

    public InvalidCostException(String message) {
        super(message);
    }
}
