package nextstep.subway.price.discount;

import java.util.Arrays;

public enum DiscountPolicy {

    CHILD(new ChildDiscountPolicy()),
    TEENAGER(new TeenagerDiscountPolicy()),
    ADULT(new AdultDiscountPolicy());

    private DiscountPolicyCalculate discountPolicyService;

    DiscountPolicy(DiscountPolicyCalculate discountPolicyService) {
        this.discountPolicyService = discountPolicyService;
    }

    public static int discount(Integer price, Integer age) {
        DiscountPolicyCalculate discountPolicyService = Arrays.stream(values())
            .map(discountPolicy -> discountPolicy.discountPolicyService)
            .filter(discount -> discount.condition(age))
            .findFirst()
            .orElse(new AdultDiscountPolicy());

        return discountPolicyService.discount(price);
    }

}
