package nextstep.subway.domain.fare;

public class LineWithExtraFarePolicy implements FarePolicy {
    private final int additionalFare;

    public LineWithExtraFarePolicy(int additionalFare) {
        this.additionalFare = additionalFare;
    }

    @Override
    public int calculateOverFare(int fare) {
        return fare + additionalFare;
    }
}
