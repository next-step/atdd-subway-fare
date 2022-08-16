package nextstep.path.domain.fare;

public class AgeDiscountFarePolicy extends FarePolicy {
    private static final int BABY_AGE_UPPER_BOUND = 6;
    private static final int CHILD_AGE_UPPER_BOUND = 13;
    private static final int TEEN_AGE_UPPER_BOUND = 19;


    public static final int FREE_FARE = 0;
    private static final double CHILD_DISCOUNT_AMOUNT = 0.5d;
    private static final double TEEN_DISCOUNT_AMOUNT = 0.2d;

    private final int age;

    AgeDiscountFarePolicy(int age, FarePolicy nextPolicy) {
        super(nextPolicy);
        this.age = age;
    }

    @Override
    public int apply(int beforeFare) {
        if (age < BABY_AGE_UPPER_BOUND) {
            return FREE_FARE;
        }

        if (age < CHILD_AGE_UPPER_BOUND) {
            return nextPolicy.apply((int) (beforeFare - beforeFare * CHILD_DISCOUNT_AMOUNT));
        }

        if (age < TEEN_AGE_UPPER_BOUND) {
            return nextPolicy.apply((int) (beforeFare - beforeFare * TEEN_DISCOUNT_AMOUNT));
        }

        return nextPolicy.apply(beforeFare);
    }
}
