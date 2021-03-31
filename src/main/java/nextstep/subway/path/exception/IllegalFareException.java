package nextstep.subway.path.exception;

public class IllegalFareException extends RuntimeException {
    private static final String MESSAGE = "요금은 기본요금보다 작을 수 없습니다.";

    public IllegalFareException() {
        super(MESSAGE);
    }
}
