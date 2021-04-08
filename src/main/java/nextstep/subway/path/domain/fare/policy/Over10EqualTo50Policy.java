package nextstep.subway.path.domain.fare.policy;

public class Over10EqualTo50Policy implements FarePolicy {

    private static final int START_BOUNDARY = 10;
    private static final int END_BOUNDARY = 50;

    private int distance;

    public Over10EqualTo50Policy() {

    }

    @Override
    public int calculate(int fare, int distance) {
        if (distance <= START_BOUNDARY) {
            return fare;
        }

        if (distance > START_BOUNDARY && distance <= END_BOUNDARY) {
            return fare + calculateOverFare(distance - START_BOUNDARY);
        }

        return fare + calculateOverFare(END_BOUNDARY - START_BOUNDARY);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
