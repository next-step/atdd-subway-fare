package nextstep.subway.domain.fare;

public class OverTenKmFare implements FarePolicy {
    public static final int DEFAULT_DISTANCE = 10;
    public static final int LESS_OR_50KM = 50;

    @Override
    public int calculateOverFare(int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return 0;
        }

        if (distance > LESS_OR_50KM) {
            return calculateOverFare(LESS_OR_50KM - DEFAULT_DISTANCE, 5);
        }

        return calculateOverFare(distance - DEFAULT_DISTANCE, 5);
    }

    private int calculateOverFare(int distance, int additionalFare) {
        return (int) ((Math.ceil((distance - 1) / additionalFare) + 1) * 100);
    }
}
