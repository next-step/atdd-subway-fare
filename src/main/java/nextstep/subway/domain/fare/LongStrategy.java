package nextstep.subway.domain.fare;

public class LongStrategy implements FareStrategy {

    private static final int BASIC_FARE = 2050;
    private static final int MIDDLE_DISTANCE = 50;

    @Override
    public int calculate(int distance) {
        int remainDistance = distance - MIDDLE_DISTANCE;
        int overFare = calculateOverFare(remainDistance);
        return BASIC_FARE + overFare;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
