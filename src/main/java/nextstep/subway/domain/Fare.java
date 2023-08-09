package nextstep.subway.domain;

public class Fare {
    private static final int BASE_FARE = 1250;
    private static final int ADDITIONAL_FARE = 100;

    private int fare;

    public Fare(int distance) {
        this.fare = calculateFare(distance);
    }

    public int getFare() {
        return fare;
    }

    private int calculateFare(int distance) {
        int additionalDistance = distance - 10;
        if (additionalDistance <= 0) {
            return BASE_FARE;
        }
        int additionalFare = (additionalDistance / 5) * ADDITIONAL_FARE;
        return BASE_FARE + additionalFare;
    }
}
