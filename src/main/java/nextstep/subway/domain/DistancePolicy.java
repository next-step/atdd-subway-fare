package nextstep.subway.domain;

public class DistancePolicy implements FarePolicy {

    public static final int DEFAULT_FARE = 1250;

    private final int distance;

    public DistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calcFare() {
        int fare = DEFAULT_FARE;

        if (distance <= 10) {
            return fare;
        }

        if (distance <= 50) {
            return DEFAULT_FARE + calculateOverFare((distance - 10), 5);
        }

        return DEFAULT_FARE + calculateOverFare((50 - 10), 5) + calculateOverFare((distance - 50), 8);
    }

    private int calculateOverFare(int distance, int km) {
        return (int) ((Math.ceil((distance - 1) / km) + 1) * 100);
    }
}
