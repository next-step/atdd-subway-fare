package nextstep.subway.domain.policy.discount;

public class YouthDiscountPolicy extends DiscountPolicy {
    private static final double RATIO = 0.8;

    public YouthDiscountPolicy(DiscountPolicy next) {
        super(next);
    }

    @Override
    protected int calculateFare(int fare) {
        return calculateFare(fare, RATIO);
    }

    @Override
    protected boolean isInAge(int age) {
        return isInAge(age, AgePolicy.YOUTH);
    }
}
