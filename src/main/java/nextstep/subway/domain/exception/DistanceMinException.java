package nextstep.subway.domain.exception;

public class DistanceMinException extends IllegalArgumentException {
    public DistanceMinException(final int min) {
        super("거리는 " + min + "이상이어야 합니다.");
    }
}
