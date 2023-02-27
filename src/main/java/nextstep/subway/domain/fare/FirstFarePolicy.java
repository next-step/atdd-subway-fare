package nextstep.subway.domain.fare;

public class FirstFarePolicy implements FarePolicy {
    @Override
    public int getFare(final int distance) {
        return DEFAULT_FARE;
    }
}
