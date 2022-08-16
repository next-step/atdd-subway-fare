package nextstep.subway.fare;

public class ChildrenFarePolicy extends AgeFarePolicy {
    private static final double RATIO = 0.5;

    public ChildrenFarePolicy(AgeFarePolicy next) {
        super(next);
    }

    @Override
    protected int calculateFare(int fare) {
        return calculateFare(fare, RATIO);
    }

    @Override
    protected boolean isInAge(int age) {
        return 6 <= age && age < 13;
    }
}
