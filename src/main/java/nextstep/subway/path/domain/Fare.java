package nextstep.subway.path.domain;

public class Fare {

    private static final int BASIC_FARE = 1250;

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int distance) {
        return new Fare(calculateFare(distance));
    }

    private static int calculateFare(int distance) {
        return BASIC_FARE + OverAddFareCalculate.calculate(distance);
    }

    public int getFare() {
        return fare;
    }
}
