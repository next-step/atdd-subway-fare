package nextstep.subway.fare;

public class YouthFarePolicy extends AgeFarePolicy{
    private static final double RATIO = 0.8;

    public YouthFarePolicy(AgeFarePolicy next) {
        super(next);
    }

    @Override
    protected int calculateFare(int fare) {
        return calculateFare(fare, RATIO);
    }

    @Override
    protected boolean isInAge(int age) {
        return 13 <= age && age < 19;
    }
}
