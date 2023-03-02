package nextstep.subway.domain.exception;

public class MinimumDistanceException extends RuntimeException {

    public MinimumDistanceException() {
        super("거리는 최소한 1 이상이여야 합니다");
    }
}
