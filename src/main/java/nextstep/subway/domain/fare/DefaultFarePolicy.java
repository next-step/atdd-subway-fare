package nextstep.subway.domain.fare;

public class DefaultFarePolicy implements FarePolicy {

    @Override
    public int getFare(final int distance) {
        return DEFAULT_FARE;
    }
}
