package nextstep.subway.domain;

public class ExtraChargeFarePolicy implements FarePolicy {
    private int extraCharge;

    private ExtraChargeFarePolicy(int extraCharge) {
        this.extraCharge = extraCharge;
    }

    public static ExtraChargeFarePolicy of(int extraCharge) {
        return new ExtraChargeFarePolicy(extraCharge);
    }

    @Override
    public int calculate(int fare) {
        return fare + extraCharge;
    }
}
