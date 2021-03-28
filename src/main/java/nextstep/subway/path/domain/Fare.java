package nextstep.subway.path.domain;

public class Fare {

    public static final int DEFAULT_FARE_DISTANCE = 10;
    public static final int ADD_100_FARE_DISTANCE = 50;
    public static final int DEFAULT_FARE = 1250;

    private int distance;
    private int fare;

    public Fare(int distance) {
        this.distance = distance;
        this.fare = calculateFare(distance);
    }

    public int getFare() {
        return fare;
    }

    private int calculateFare(int distance) {
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (distance <= ADD_100_FARE_DISTANCE) {
            return DEFAULT_FARE + calculateOverFareBefore50(distance);
        }
        return DEFAULT_FARE + calculateOverFareBefore50(distance)
                + calculateOverFareAfter50(distance);
    }

    protected int calculateOverFareBefore50(int distance) {
        return (int) ((Math.ceil((distance - DEFAULT_FARE_DISTANCE - 1) / 5) + 1) * 100);
    }

    protected int calculateOverFareAfter50(int distance) {
        return (int) ((Math.ceil((distance - ADD_100_FARE_DISTANCE - 1) / 8) + 1) * 100);
    }

}
