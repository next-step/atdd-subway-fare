package nextstep.subway.domain;

public class ExtraFarePolicy extends FarePolicy {
    private int extraFare;

    ExtraFarePolicy(int extraFare) {
        this.extraFare = extraFare;
    }

    @Override
    protected int calculateFare(int fare) {
        return fare + extraFare;
    }
}
