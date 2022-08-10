package nextstep.member.domain.exception;

public class DuplicateFavoriteException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "이미 존재하는 즐겨찾기입니다";

    public DuplicateFavoriteException() {
        super(DEFAULT_MESSAGE);
    }
}
