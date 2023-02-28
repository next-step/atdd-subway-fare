package nextstep.subway.domain;

public class DistancePolicy implements FarePolicy {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int MIDDLE_DISTANCE = 50;
    private static final int MIDDLE_PER_DISTANCE_POLICY = 5;
    private static final int EIGHT_KM_DISTANCE_POLICY = 8;

    private final int distance;

    public DistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calcFare() {
        if (distance <= DEFAULT_DISTANCE) {
            return firstDistanceFarePolicy();
        }

        if (distance <= MIDDLE_DISTANCE) {
            return middleDistanceFarePolicy();
        }

        return lastDistanceFarePolicy();
    }

    private static int firstDistanceFarePolicy() {
        return DEFAULT_FARE;
    }

    private int middleDistanceFarePolicy() {
        return DEFAULT_FARE + calculateOverFare((distance - DEFAULT_DISTANCE), MIDDLE_PER_DISTANCE_POLICY);
    }

    private int lastDistanceFarePolicy() {
        return DEFAULT_FARE + calculateOverFare((MIDDLE_DISTANCE - DEFAULT_DISTANCE), MIDDLE_PER_DISTANCE_POLICY) + calculateOverFare((distance - MIDDLE_DISTANCE), EIGHT_KM_DISTANCE_POLICY);

    }

    private int calculateOverFare(int distance, int perDistance) {
        return (int) ((Math.ceil((distance - 1) / perDistance) + 1) * 100);
    }
}
