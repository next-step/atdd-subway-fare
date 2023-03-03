package nextstep.subway.domain.Fare;

public class FarePolicy {
    private final static int DEFAULT_FARE = 1250;
    private final static int EXTRA_FARE = 100;
    private final static int FIRST_FARE_SECTION = 10;
    private final static int SECOND_FARE_SECTION = 50;

    public static int getFare(int distance) {
        int fare = DEFAULT_FARE;
        if (distance > FIRST_FARE_SECTION && distance <= SECOND_FARE_SECTION) {
            fare += (int) ((Math.ceil((distance - FIRST_FARE_SECTION -1) / 5) + 1) * EXTRA_FARE);
        }
        if (distance > SECOND_FARE_SECTION) {
            fare += (int) (800 + (Math.ceil((distance - SECOND_FARE_SECTION - 1) / 8) + 1) * EXTRA_FARE);
        }
        return fare;
    }
}
