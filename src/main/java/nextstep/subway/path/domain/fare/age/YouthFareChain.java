package nextstep.subway.path.domain.fare.age;

public class YouthFareChain implements AgeChain{

    private static final int YOUTH_AGE_MIN = 13;
    private static final int YOUTH_AGE_MAX = 19;
    private static final int YOUTH_DEFAULT_DISCOUNT = 350;
    private static final double YOUTH_DISCOUNT_PERCENTAGE = 0.2;

    @Override
    public boolean findAgeGroup(int age) {
        return age >= YOUTH_AGE_MIN && age < YOUTH_AGE_MAX;
    }

    @Override
    public int calculate(int fare) {
        return (int) ((fare - YOUTH_DEFAULT_DISCOUNT) * (1 - YOUTH_DISCOUNT_PERCENTAGE));
    }
}
