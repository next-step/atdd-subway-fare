package nextstep.subway.path.domain.policy;


public class AgeDiscountPolicy {
    private static final int SIXTEEN = 6;
    private static final int THIRTEEN = 13;
    private static final int NINETEEN = 19;
    private static final double YOUTH_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    public static FarePolicy getAgeDiscountPolicy(int age) {
        if (age < THIRTEEN && age >= SIXTEEN) {
            return new ChildrenFareDiscountPolicy();
        }
        if (age < NINETEEN && age >= THIRTEEN) {
            return new YouthFareDiscountPolicy();
        }
        return new DefaultDiscount();
    }

    private static int calculate(int fare, double discountRate) {
        return (int) (fare - Math.ceil((fare - 350) * discountRate));
    }

    public static class DefaultDiscount implements FarePolicy {
        @Override
        public int fareCalculate(int fare) {
            return fare;
        }
    }

    public static class YouthFareDiscountPolicy implements FarePolicy {

        @Override
        public int fareCalculate(int fare) {
            return calculate(fare, YOUTH_DISCOUNT_RATE);
        }
    }

    public static class ChildrenFareDiscountPolicy implements FarePolicy {
        @Override
        public int fareCalculate(int fare) {
            return calculate(fare, CHILDREN_DISCOUNT_RATE);
        }
    }

}
