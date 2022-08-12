package nextstep.member.domain.exception;

public class DuplicateEmailException extends IllegalArgumentException {
    public static final String DEFAULT_MESSAGE = "이미 존재하는 이메일입니다";

    public DuplicateEmailException() {
        super(DEFAULT_MESSAGE);
    }
}
