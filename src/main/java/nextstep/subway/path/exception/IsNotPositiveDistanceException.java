package nextstep.subway.path.exception;

public class IsNotPositiveDistanceException extends RuntimeException {
    public static final String MESSAGE = "운행 거리는 1이상만 가능합니다.";
    public IsNotPositiveDistanceException() {
        super(MESSAGE);
    }
}
