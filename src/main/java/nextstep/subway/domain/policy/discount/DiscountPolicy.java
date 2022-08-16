package nextstep.subway.domain.policy.discount;

public abstract class DiscountPolicy {
    private static final int EXCLUDED_FARE = 350;
    private final DiscountPolicy next;

    public DiscountPolicy(DiscountPolicy next) {
        this.next = next;
    }

    public final int calculate(int age, int fare) {
        if (isInAge(age)) {
            return calculateFare(fare);
        }
        if (next != null) {
            return next.calculate(age, fare);
        }
        return fare;
    }

    public int calculateFare(int fare, double ratio) {
        return (int) ((fare - EXCLUDED_FARE) * ratio) + EXCLUDED_FARE;
    }

    public boolean isInAge(int age, AgePolicy agePolicy) {
        return agePolicy.getMinAge() <= age && age < agePolicy.getMaxAge();
    }
    protected abstract int calculateFare(int fare);

    protected abstract boolean isInAge(int age);

}
