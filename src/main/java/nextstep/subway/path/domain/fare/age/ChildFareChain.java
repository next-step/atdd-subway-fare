package nextstep.subway.path.domain.fare.age;

public class ChildFareChain implements AgeChain{

    private static final int CHILD_AGE_MIN = 6;
    private static final int CHILD_AGE_MAX = 13;
    private static final int CHILD_DEFAULT_DISCOUNT = 350;
    private static final double CHILD_DISCOUNT_PERCENTAGE = 0.5;

    @Override
    public boolean findAgeGroup(int age) {
        return (age >= CHILD_AGE_MIN && age < CHILD_AGE_MAX);
    }

    @Override
    public int calculate(int fare) {
        return (int)((fare - CHILD_DEFAULT_DISCOUNT) * (1 - CHILD_DISCOUNT_PERCENTAGE));
    }
}
