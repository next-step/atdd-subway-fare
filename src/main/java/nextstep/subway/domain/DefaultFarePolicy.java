package nextstep.subway.domain;

public class DefaultFarePolicy implements SubwayFarePolicy {
    private static final int defaultFare = 1250;

    private final int fare;

    public DefaultFarePolicy() {
        this(defaultFare);
    }

    public DefaultFarePolicy(int fare) {
        this.fare = fare;
    }

    @Override
    public int apply(int currentFare, int distance) {
        return fare;
    }
}
