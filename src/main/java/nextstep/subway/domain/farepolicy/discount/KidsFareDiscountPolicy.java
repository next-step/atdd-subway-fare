package nextstep.subway.domain.farepolicy.discount;

public class KidsFareDiscountPolicy implements FareDiscountPolicy {
    private static final int FARE_FOR_EXCLUDED_DISCOUNT  = 350;
    private static final int DISCOUNT_PERCENT_FOR_CHILD = 50;
    private static final int DISCOUNT_PERCENT_FOR_TEENAGER = 20;
    private static final AgeRange CHILD_AGE = new AgeRange(6, 13);
    private static final AgeRange TEENAGER_AGE = new AgeRange(13, 19);

    private final int age;

    public KidsFareDiscountPolicy(int age) {
        this.age = age;
    }

    @Override
    public int discount(int totalCost) {
        if (CHILD_AGE.isWithin(age)) {
            return calculate(totalCost, DISCOUNT_PERCENT_FOR_CHILD);
        }
        if (TEENAGER_AGE.isWithin(age)) {
            return calculate(totalCost, DISCOUNT_PERCENT_FOR_TEENAGER);
        }
        return totalCost;
    }

    private int calculate(int totalCost, int discountPercent) {
        int deductedFare = totalCost - FARE_FOR_EXCLUDED_DISCOUNT;
        return (int)(deductedFare / 100.0d * (100 - discountPercent));
    }
}
