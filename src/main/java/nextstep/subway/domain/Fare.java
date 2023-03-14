package nextstep.subway.domain;

public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private final int amount;

    public Fare(final int distance) {
        this.amount = calculateAmount(distance);
    }

    public int getAmount() {
        return amount;
    }

    private int calculateAmount(final int distance) {
        if (distance <= 10) {
            return DEFAULT_FARE;
        }
        else if (distance > 10 && distance <= 50) {
            return DEFAULT_FARE + calculateOverFare(distance - 10, 5);
        }
        else {
            return DEFAULT_FARE + calculateOverFare(40, 5) + calculateOverFare(distance - 50, 8);
        }
    }

    private int calculateOverFare(final int distance, final int per_distance) {
        return (int) ((Math.ceil((distance - 1) / per_distance) + 1) * 100);
    }
}
