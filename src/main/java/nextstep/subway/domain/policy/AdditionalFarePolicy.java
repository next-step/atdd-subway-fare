package nextstep.subway.domain.policy;

public class AdditionalFarePolicy extends FarePolicy {
    private int additionalFare;

    public AdditionalFarePolicy(int additionalFare, FarePolicy farePolicy) {
        super(farePolicy);
        this.additionalFare = additionalFare;
    }

    @Override
    public int calculate() {
        return super.calculate() + additionalFare;
    }
}
