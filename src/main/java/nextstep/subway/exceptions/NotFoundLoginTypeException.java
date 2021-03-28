package nextstep.subway.exceptions;

public class NotFoundLoginTypeException extends RuntimeException{
    public static final String DEFAULT_MSG= "유효하지 않은 로그인 타입입니다.";

    public NotFoundLoginTypeException() {
        super(DEFAULT_MSG);
    }

    public NotFoundLoginTypeException(String message) {
        super(message);
    }
}
