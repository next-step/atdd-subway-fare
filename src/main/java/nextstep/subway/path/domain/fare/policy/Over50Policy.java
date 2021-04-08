package nextstep.subway.path.domain.fare.policy;

public class Over50Policy implements FarePolicy {

    private static final int START_BOUNDARY = 50;

    private int distance;

    public Over50Policy() {

    }

    @Override
    public int calculate(int fare, int distance) {
        if (distance <= START_BOUNDARY) {
            return fare;
        }

        return fare + calculateOverFare(distance - START_BOUNDARY);
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
