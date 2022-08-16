package nextstep.subway.domain.policy.payment;

import nextstep.subway.domain.policy.discount.ChildrenDiscountPolicy;
import nextstep.subway.domain.policy.discount.DiscountPolicy;
import nextstep.subway.domain.policy.discount.YouthDiscountPolicy;
import nextstep.subway.domain.policy.over.Over10FarePolicy;
import nextstep.subway.domain.policy.over.Over50FarePolicy;
import nextstep.subway.domain.policy.over.OverFarePolicy;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;

public class PaymentUtil {
    private static final int DEFAULT_FARE = 1250;

    private PaymentUtil() {
        throw new IllegalArgumentException("Util 클래스는 생성자를 생성할 수 없습니다.");
    }

    private static final OverFarePolicy overFarePolicy = new Over10FarePolicy(new Over50FarePolicy(null));
    private static final DiscountPolicy discountPolicy = new ChildrenDiscountPolicy(new YouthDiscountPolicy(null));

    public static int getFare(int distance, int additionalCharge) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int overFare = DEFAULT_FARE + overFarePolicy.calculate(distance) + additionalCharge;

        if (authentication == null) {
            return overFare;
        }

        return discountPolicy.calculate(authentication.getAge(), overFare);
    }

}
