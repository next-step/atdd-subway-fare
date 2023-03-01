package nextstep.subway.domain.Fare;

public class FarePolicy {
    private final static int DEFAULT_FARE = 1250;
    private final static int EXTRA_FARE = 100;
    public static int getFare(int distance) {
        int fare = DEFAULT_FARE;
        if (distance > 10 && distance <= 50) {
            fare += (int) ((Math.ceil((distance - 11) / 5) + 1) * EXTRA_FARE);
        }
        if (distance > 50) {
            fare += (int) (800 + (Math.ceil((distance - 51) / 8) + 1) * EXTRA_FARE);
        }
        return fare;
    }
}
