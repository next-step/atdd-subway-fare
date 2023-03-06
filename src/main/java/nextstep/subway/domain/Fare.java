package nextstep.subway.domain;

public class Fare {
    public static final int BASIC_FARE = 1_250;
    private final int fare;

    public Fare(int distance) {
        if (distance > 10) {
            this.fare = BASIC_FARE + calculateOverFare(distance);
            return;
        }

        this.fare = BASIC_FARE;
    }

    private int calculateOverFare(int distance) {
        int additionalDistance = 0;
        int additionalFare = 0;

        if (distance > 50) {
            additionalDistance = distance - 50;
            additionalFare = (int) ((Math.ceil((additionalDistance - 1) / 8) + 1) * 100);
        }

        return (int) ((Math.ceil((distance - additionalDistance - 10 - 1) / 5) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
