package nextstep.subway.domain.policy.discount;

import nextstep.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

public class DiscountManager {

    private static final List<DiscountPolicy> policies = new ArrayList<>();

    private DiscountManager() {
    }

    public static void addPolicy(DiscountPolicy discountPolicy) {
        policies.add(discountPolicy);
    }

    public static void clearPolicy() {
        if (policies.size() > 0) {
            policies.clear();
        }
    }

    public static int discount(int fare, Member member) {
        DiscountPolicy policy = findDiscount(member);
        return policy.discount(fare);
    }

    private static DiscountPolicy findDiscount(Member member) {
        return policies.stream().parallel()
                .filter(discountPolicy -> discountPolicy.supports(member))
                .findAny()
                .orElse(new NotDiscount());
    }

}
