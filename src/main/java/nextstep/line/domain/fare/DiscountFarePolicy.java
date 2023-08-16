package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;

public interface DiscountFarePolicy {

    boolean isSupport(UserPrincipal userPrincipal);

    Integer getFare(Integer fare);

}
