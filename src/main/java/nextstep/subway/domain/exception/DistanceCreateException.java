package nextstep.subway.domain.exception;

public class DistanceCreateException extends IllegalArgumentException {

    public DistanceCreateException(final int min) {
        super("구간은 " + min + "이상이어야 합니다.");
    }
}
