package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;

public class AdultDiscountPolicy implements DiscountFarePolicy {

    @Override
    public boolean isSupport(UserPrincipal userPrincipal) {
        return userPrincipal instanceof UserPrincipal && userPrincipal.isAdult();
    }

    @Override
    public Integer getFare(Integer fare) {
        return fare;
    }
}
