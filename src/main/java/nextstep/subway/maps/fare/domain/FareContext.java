package nextstep.subway.maps.fare.domain;

public class FareContext {
    public static final int DEFAULT_FARE = 1250;
    private final int distance;
    private final Fare fare;

    public FareContext(int distance) {
        this.distance = distance;
        this.fare = new Fare(DEFAULT_FARE);
    }

    public int getDistance() {
        return this.distance;
    }

    public Fare getFare() {
        return this.fare;
    }
}
