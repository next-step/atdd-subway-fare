package nextstep.subway.price.discount;

public interface DiscountPolicyService {

    boolean condition(Integer age);

    Integer discount(Integer price);
}
