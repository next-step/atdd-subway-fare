package nextstep.subway.domain;

public class DistanceFarePolicy implements FarePolicy {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int MIDDLE_DISTANCE = 50;
    private static final int MIDDLE_PER_DISTANCE_POLICY = 5;
    private static final int EIGHT_KM_DISTANCE_POLICY = 8;

    private final int distance;

    public DistanceFarePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calcFare(int currentFare) {
        if (distance <= DEFAULT_DISTANCE) {
            return currentFare;
        }

        if (distance <= MIDDLE_DISTANCE) {
            return currentFare + middleDistanceFarePolicy();
        }

        return currentFare + lastDistanceFarePolicy();
    }

    private int middleDistanceFarePolicy() {
        return calculateOverFare((distance - DEFAULT_DISTANCE), MIDDLE_PER_DISTANCE_POLICY);
    }

    private int lastDistanceFarePolicy() {
        return calculateOverFare((MIDDLE_DISTANCE - DEFAULT_DISTANCE), MIDDLE_PER_DISTANCE_POLICY) + calculateOverFare((distance - MIDDLE_DISTANCE), EIGHT_KM_DISTANCE_POLICY);

    }

    private int calculateOverFare(int distance, int perDistance) {
        return (int) ((Math.ceil((distance - 1) / perDistance) + 1) * 100);
    }

}
