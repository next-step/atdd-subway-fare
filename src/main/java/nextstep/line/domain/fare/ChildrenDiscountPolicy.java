package nextstep.line.domain.fare;

import nextstep.member.domain.Member;

public class ChildrenDiscountPolicy implements DiscountFarePolicy {

    @Override
    public boolean isSupport(Member member) {
        return member.isChildren();
    }

    @Override
    public Integer getFare(Integer fare) {
        return (int) ((fare - 350) * 0.5);
    }
}
