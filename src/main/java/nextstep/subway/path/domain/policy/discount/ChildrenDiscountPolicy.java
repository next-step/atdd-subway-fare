package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.DiscountPolicy;

public class ChildrenDiscountPolicy implements DiscountPolicy {

    @Override
    public int discount(int fare) {
        int resultFare = (int) (fare -  Math.ceil((fare - 350) * 0.5));
        return resultFare;
    }
}
