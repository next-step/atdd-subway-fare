package nextstep.line.domain.fare;

import nextstep.member.domain.Member;

public class AnonymousDiscountPolicy implements DiscountFarePolicy {

    @Override
    public boolean isSupport(Member member) {
        return member == null;
    }

    @Override
    public Integer getFare(Integer fare) {
        return fare;
    }
}
