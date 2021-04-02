package nextstep.subway.path.domain.policy.discount;

import nextstep.subway.path.domain.policy.FarePolicy;

public class ChildrenDiscountPolicy implements FarePolicy {

    @Override
    public int calculateFare(int fare) {
        int resultFare = (int) (fare -  Math.ceil((fare - 350) * 0.5));
        return resultFare;
    }
}
