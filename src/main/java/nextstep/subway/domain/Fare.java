package nextstep.subway.domain;

public class Fare {
    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public int calculateOverFare() {
        return FareStandard.calculateOverFare(distance);
    }
}