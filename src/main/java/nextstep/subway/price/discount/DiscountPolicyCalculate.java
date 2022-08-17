package nextstep.subway.price.discount;

public interface DiscountPolicyCalculate {

    boolean condition(Integer age);

    Integer discount(Integer price);
}
