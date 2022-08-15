package nextstep.subway.price.discount;

public class TeenagerDiscountPolicy implements DiscountPolicyService {

    @Override
    public boolean condition(Integer age) {
        return age >= 13 && age < 19;
    }

    @Override
    public Integer discount(Integer price) {
        return (int) ((price - 350) * 0.8);
    }
}
