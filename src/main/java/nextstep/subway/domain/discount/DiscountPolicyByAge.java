package nextstep.subway.domain.discount;

import nextstep.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class DiscountPolicyByAge implements DiscountPolicy {

    @Override
    public long discountFare(Member member, long fare) {
        return DiscountPolicyByAgeCalculator.applyToDiscountFare(member, fare);
    }
}
