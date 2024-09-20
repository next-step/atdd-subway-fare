package nextstep.fare.domain;

public class LessThanMinimumDistanceException extends IllegalStateException {

    private static final String MESSAGE = "거리는 %s 이상이어야 합니다. 현재 거리: %s";

    public LessThanMinimumDistanceException(int minimumDistance, int distance) {
        super(String.format(MESSAGE, minimumDistance, distance));
    }
}