package nextstep.subway.domain.exception;

public class PathNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "경로가 존재하지 않습니다.";

    public PathNotFoundException() {
        super(MESSAGE);
    }
}
