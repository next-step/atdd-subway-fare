package nextstep.subway.price.discount;

public class ChildDiscountPolicy implements DiscountPolicyCalculate {
    private static final int SIX_AGE = 6;
    private static final int THIRTEEN_AGE = 13;

    private static final int DEFAULT_DISCOUNT_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.5;


    @Override
    public boolean condition(Integer age) {
        return age >= SIX_AGE && age < THIRTEEN_AGE;
    }

    @Override
    public Integer discount(Integer price) {
        return (int) ((price - DEFAULT_DISCOUNT_AMOUNT) * DISCOUNT_RATE);
    }
}
