package nextstep.subway.domain.fare;

public class MiddleStrategy implements FareStrategy {

    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;

    @Override
    public int calculate(int distance) {
        int remainDistance = distance - BASIC_DISTANCE;
        int overFare = calculateOverFare(remainDistance);
        return BASIC_FARE + overFare;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
