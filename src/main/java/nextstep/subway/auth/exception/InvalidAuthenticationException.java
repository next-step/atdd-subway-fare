package nextstep.subway.auth.exception;

public class InvalidAuthenticationException extends RuntimeException {
    public static final String MESSAGE = "인증정보가 없거나 유효하지 않습니다.";

    public InvalidAuthenticationException() {
        super(MESSAGE);
    }
}
