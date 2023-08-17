package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.domain.Member;

public class AdultDiscountPolicy implements DiscountFarePolicy {

    @Override
    public boolean isSupport(Member member) {
        return member.isAdult();
    }

    @Override
    public Integer getFare(Integer fare) {
        return fare;
    }
}
