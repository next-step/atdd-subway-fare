package nextstep.subway.domain.policy.calculate;

public class GreaterUnitFarePolicy implements FareCalculatePolicy {

    private final int factor;
    private final int unit;
    private final int fare;


    public GreaterUnitFarePolicy(int factor, int unit, int fare) {
        this.factor = factor;
        this.unit = unit;
        this.fare = fare;
    }

    @Override
    public int calculate(CalculateConditions conditions) {
        if (conditions.getDistance() <= factor) {
            return 0;
        }
        return (((conditions.getDistance() - factor - 1) / unit) + 1) * fare;
    }

}
