package nextstep.subway.domain.policy;

public class GreaterUnitFarePolicy implements FarePolicy {

    private final int factor;
    private final int unit;
    private final int fare;


    public GreaterUnitFarePolicy(int factor, int unit, int fare) {
        this.factor = factor;
        this.unit = unit;
        this.fare = fare;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= factor) {
            return 0;
        }
        return (((distance - factor - 1) / unit) + 1) * fare;
    }

}
