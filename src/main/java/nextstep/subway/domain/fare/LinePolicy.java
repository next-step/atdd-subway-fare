package nextstep.subway.domain.fare;

public class LinePolicy implements FarePolicy {
    private static final LinePolicy INSTANCE = new LinePolicy();

    private LinePolicy() {
    }

    public static LinePolicy getInstance() {
        return INSTANCE;
    }

    @Override
    public Fare addFare(Fare fare, FareBasis fareBasis) {
        return fare.addExtraLineFare(fareBasis.getLineCharge());
    }
}
