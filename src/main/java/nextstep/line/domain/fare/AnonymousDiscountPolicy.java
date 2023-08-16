package nextstep.line.domain.fare;

import nextstep.auth.principal.AnonymousPrincipal;
import nextstep.auth.principal.UserPrincipal;

public class AnonymousDiscountPolicy implements DiscountFarePolicy {

    @Override
    public boolean isSupport(UserPrincipal userPrincipal) {
        return userPrincipal instanceof AnonymousPrincipal;
    }

    @Override
    public Integer getFare(Integer fare) {
        return fare;
    }
}
