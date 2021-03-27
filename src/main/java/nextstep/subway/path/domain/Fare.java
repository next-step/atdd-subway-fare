package nextstep.subway.path.domain;

public class Fare {

    private final int BASIC_FARE = 1250;
    private final int fare;

    public Fare(int distance) {
        this.fare = BASIC_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        DistanceOfFareType distanceOfFareType = DistanceOfFareType.valueOf(distance);
        return distanceOfFareType.calculate(distance);
    }

    public int getFare() {
        return fare;
    }
}
