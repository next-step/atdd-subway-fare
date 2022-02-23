package nextstep.subway.domain;

public class SubwayFare {
    private static final int DEFAULT_FARE = 1250;

    private final int distance;

    public SubwayFare(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance <= 10) {
            return DEFAULT_FARE;
        }

        if (distance <= 50) {
            return DEFAULT_FARE + calculateOverFare(5, distance - 10);
        }

        return DEFAULT_FARE + calculateOverFare(5, 40) + calculateOverFare(8, distance - 50);
    }

    private int calculateOverFare(int step, int distance) {
        return (int) ((Math.ceil((distance - 1) / step) + 1) * 100);
    }
}
