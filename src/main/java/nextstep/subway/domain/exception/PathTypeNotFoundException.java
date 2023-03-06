package nextstep.subway.domain.exception;

public class PathTypeNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "경로 타입을 찾을 수 없습니다.";

    public PathTypeNotFoundException() {
        super(MESSAGE);
    }
}
