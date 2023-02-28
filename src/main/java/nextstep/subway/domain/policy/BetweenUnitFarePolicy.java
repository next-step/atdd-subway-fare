package nextstep.subway.domain.policy;

public class BetweenUnitFarePolicy implements FarePolicy {

    private final int start;
    private final int end;
    private final int unit;
    private final int fare;


    public BetweenUnitFarePolicy(int start, int end, int unit, int fare) {
        this.start = start;
        this.end = end;
        this.unit = unit;
        this.fare = fare;
    }

    @Override
    public int calculate(CalculateConditions conditions) {
        if (conditions.getDistance() <= start) {
            return 0;
        }
        if (conditions.getDistance() > end) {
            return (((end - start - 1) / unit) + 1) * fare;
        }
        return (((conditions.getDistance() - start - 1) / unit) + 1) * fare;
    }

}
