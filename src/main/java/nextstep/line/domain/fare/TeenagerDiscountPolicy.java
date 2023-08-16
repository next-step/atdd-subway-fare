package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;

public class TeenagerDiscountPolicy implements DiscountFarePolicy {

    @Override
    public boolean isSupport(UserPrincipal userPrincipal) {
        return userPrincipal instanceof UserPrincipal && userPrincipal.isTeenager();
    }

    @Override
    public Integer getFare(Integer fare) {
        return (int) ((fare - 350) * 0.8);
    }
}
