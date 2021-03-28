package nextstep.subway.exceptions;

public class NotFoundAgeTypeException extends RuntimeException{
    public static final String DEFAULT_MSG = "해당 나이에 맞는 CostByAge 타입을 찾을 수 없습니다.";

    public NotFoundAgeTypeException() {
        super(DEFAULT_MSG);
    }

    public NotFoundAgeTypeException(String message) {
        super(message);
    }
}
