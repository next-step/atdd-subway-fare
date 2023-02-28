package nextstep.subway.domain;

public class DistancePolicy implements FarePolicy {

    private final int distance;

    public DistancePolicy(int distance) {
        this.distance = distance;
    }

    @Override
    public int calcFare() {
        return 0;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
