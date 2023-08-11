package nextstep.subway.domain;

public class DiscountFarePolicy implements FarePolicy {
    private static final float YOUTH_DISCOUNT_RATE = 0.2f;
    private static final float CHILD_DISCOUNT_RATE = 0.5f;
    private static final int LITTLE_CHILD_FARE = 0;
    private static final int DEDUCTED_AMOUNT = 350;
    private int age;

    private DiscountFarePolicy(int age) {
        this.age = age;
    }

    public static DiscountFarePolicy of(int age) {
        return new DiscountFarePolicy(age);
    }

    @Override
    public int calculate(int fare) {
        if (this.age < 6) {
            return LITTLE_CHILD_FARE;
        } else if (this.age < 13) {
            return discount(fare, CHILD_DISCOUNT_RATE);
        } else if (this.age < 19) {
            return discount(fare, YOUTH_DISCOUNT_RATE);
        }
        return fare;
    }

    private int discount(int fare, float discount_rate) {
        return fare - Math.round((fare - DEDUCTED_AMOUNT) * discount_rate);
    }
}
