package nextstep.subway.price.discount;

import java.util.Arrays;

public enum DiscountPolicy {

    CHILD(new ChildDiscountPolicy()),
    TEENAGER(new TeenagerDiscountPolicy()),
    ADULT(new AdultDiscountPolicy());

    private DiscountPolicyService discountPolicyService;

    DiscountPolicy(DiscountPolicyService discountPolicyService) {
        this.discountPolicyService = discountPolicyService;
    }

    public static int discount(Integer price, Integer age) {
        DiscountPolicyService discountPolicyService = Arrays.stream(values())
            .map(discountPolicy -> discountPolicy.discountPolicyService)
            .filter(discount -> discount.condition(age))
            .findFirst()
            .orElse(new AdultDiscountPolicy());

        return discountPolicyService.discount(price);
    }

}
