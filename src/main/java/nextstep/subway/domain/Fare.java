package nextstep.subway.domain;

public class Fare {

    private final FarePolicy farePolicy;

    private final int totalDistance;

    private Fare(FarePolicy farePolicy, int totalDistance) {
        this.farePolicy = farePolicy;
        this.totalDistance = totalDistance;
    }

    public static Fare of(FarePolicy farePolicy, int totalDistance) {
        return new Fare(farePolicy, totalDistance);
    }

    public int get() {
        return farePolicy.calculateFare(totalDistance);
    }
}
