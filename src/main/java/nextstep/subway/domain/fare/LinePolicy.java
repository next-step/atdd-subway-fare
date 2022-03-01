package nextstep.subway.domain.fare;


public class LinePolicy implements FarePolicy {

    private final int expensiveExtraCharge;

    public static LinePolicy from(final int expensiveExtraCharge) {
        return new LinePolicy(expensiveExtraCharge);
    }

    private LinePolicy(final int expensiveExtraCharge) {
        this.expensiveExtraCharge = expensiveExtraCharge;
    }

    @Override
    public void calculate(Fare fare) {
        fare.add(expensiveExtraCharge);
    }
}
