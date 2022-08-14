package nextstep.subway.domain.fare;

public class BasicFarePolicy implements FarePolicy {

    private static final long BASIC_FARE = 1250;

    @Override
    public long calculateOverFare(int distance) {
        if(distance <= 10) return BASIC_FARE;
        if(distance > 10) distance -= 10;
        return BASIC_FARE + (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
