package nextstep.subway.domain.fare;

public class DistanceFare implements FarePolicy {
    public static final int DEFAULT_DISTANCE = 10;
    public static final int FIFTY_DISTANCE = 50;
    private final int distance;

    public DistanceFare(int distance) {
        this.distance = distance;
    }

    @Override
    public int calculateOverFare(int fare) {
        if (distance <= DEFAULT_DISTANCE) {
            return fare;
        }

        if (distance <= FIFTY_DISTANCE) {
            return fare + upToFiftyDistancePolicy();
        }

        return fare + overFiftyDistancePolicy();
    }

    private int upToFiftyDistancePolicy() {
        return calculateOverFare(distance - DEFAULT_DISTANCE, 5);
    }

    private int overFiftyDistancePolicy() {
        return calculateOverFare(FIFTY_DISTANCE - DEFAULT_DISTANCE, 5) + calculateOverFare(distance - FIFTY_DISTANCE, 8);
    }

    private int calculateOverFare(int distance, int additionalFare) {
        return (int) ((Math.ceil((distance - 1) / additionalFare) + 1) * 100);
    }
}
