package nextstep.subway.price.discount;

public class TeenagerDiscountPolicy implements DiscountPolicyCalculate {
    private static final int THIRTEEN_AGE = 13;
    private static final int NINETEEN_AGE = 19;

    private static final int DEFAULT_DISCOUNT_AMOUNT = 350;
    private static final double DISCOUNT_RATE = 0.8;


    @Override
    public boolean condition(Integer age) {
        return age >= THIRTEEN_AGE && age < NINETEEN_AGE;
    }

    @Override
    public Integer discount(Integer price) {
        return (int) ((price - DEFAULT_DISCOUNT_AMOUNT) * DISCOUNT_RATE);
    }
}
