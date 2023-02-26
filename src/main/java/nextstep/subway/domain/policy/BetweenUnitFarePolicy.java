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
    public int calculate(int distance) {
        if (distance <= start) {
            return 0;
        }
        if (distance > end) {
            return (((end - start - 1) / unit) + 1) * fare;
        }
        return (((distance - start - 1) / unit) + 1) * fare;
    }

}
