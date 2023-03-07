package nextstep.subway.domain.exception;

public class DurationCreateException extends IllegalArgumentException {
    public DurationCreateException(final int min) {
        super("시간은 " + min + "이상이어야 합니다.");
    }
}
