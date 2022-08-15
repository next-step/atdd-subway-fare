package nextstep.subway.price.discount;

public class AdultDiscountPolicy implements DiscountPolicyService {

    @Override
    public boolean condition(Integer age) {
        return age >= 19;
    }

    @Override
    public Integer discount(Integer price) {
        return price;
    }
}
