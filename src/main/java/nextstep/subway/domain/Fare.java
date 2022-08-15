package nextstep.subway.domain;

public class Fare {

    private static final int BASIC_FARE = 1_250;

    private static final int BASIC_FARE_KM = 10;

    private static final int _100WON_PER_5KM_BASED_KM = 50;

    private static final int EXTRA_FARE_BASIC_UNIT = 100;

    private int basicFare;

    private int distance;

    public Fare(int distance) {
        this(0, distance);
    }

    public Fare(int lineFare, int distance) {
        this.basicFare = BASIC_FARE + lineFare;
        this.distance = distance;
    }

    public int calculate() {
        return basicFare + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        if (distance <= BASIC_FARE_KM) {
            return 0;
        }

        if (distance <= _100WON_PER_5KM_BASED_KM) {
            return calculateOverFare(BASIC_FARE_KM, distance, 5);
        }

        int extraFareUnder50Km = calculateOverFare(BASIC_FARE_KM, _100WON_PER_5KM_BASED_KM, 5);
        int extraFareOver50Km = calculateOverFare(_100WON_PER_5KM_BASED_KM, distance, 8);

        return extraFareUnder50Km + extraFareOver50Km;

    }

    private int calculateOverFare(int from, int to, int divisor) {
        return (int) ((Math.ceil((to - from - 1) / divisor) + 1) * EXTRA_FARE_BASIC_UNIT);
    }
}
