package nextstep.subway.domain.fare;

public class DiscountFareByAgeGroup implements FarePolicy {
    public static final int CHILDREN_AGE_MIN = 6;
    public static final int CHILDREN_AGE_MAX = 13;
    public static final int TEENAGER_AGE_MIN = 13;
    public static final int TEENAGER_AGE_MAX = 19;
    public static final int DEFAULT_DEDUCT_FARE = 350;
    public static final double CHILD_DISCOUNT_RATE = 0.5;
    public static final double TEENAGER_DISCOUNT_RATE = 0.8;
    private final int age;

    public DiscountFareByAgeGroup(int age) {
        this.age = age;
    }

    @Override
    public int calculateOverFare(int fare) {
        if (isChild()) {
            return (int) ((fare - DEFAULT_DEDUCT_FARE) * CHILD_DISCOUNT_RATE);
        }

        if (isTeenager()) {
            return (int) ((fare - DEFAULT_DEDUCT_FARE) * TEENAGER_DISCOUNT_RATE);
        }

        return fare;
    }

    private boolean isChild() {
        return CHILDREN_AGE_MIN <= this.age && this.age < CHILDREN_AGE_MAX;
    }

    private boolean isTeenager() {
        return TEENAGER_AGE_MIN <= this.age && this.age < TEENAGER_AGE_MAX;
    }
}
