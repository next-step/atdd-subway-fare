package nextstep.subway.price.discount;

public class AdultDiscountPolicy implements DiscountPolicyCalculate {
    private final static int NINETEEN_AGE = 19;

    @Override
    public boolean condition(Integer age) {
        return age >= NINETEEN_AGE;
    }

    @Override
    public Integer discount(Integer price) {
        return price;
    }
}
