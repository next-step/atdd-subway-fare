package nextstep.subway.price.discount;

public class ChildDiscountPolicy implements DiscountPolicyService {

    @Override
    public boolean condition(Integer age) {
        return age >= 6 && age < 13;
    }

    @Override
    public Integer discount(Integer price) {
        return (int) ((price - 350) * 0.5);
    }
}
