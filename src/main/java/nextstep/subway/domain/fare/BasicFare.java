package nextstep.subway.domain.fare;

public class BasicFare implements FarePolicy {
    public static final int BASIC_FARE = 1_250;

    @Override
    public int calculateOverFare(int distance) {
        return BASIC_FARE;
    }
}
