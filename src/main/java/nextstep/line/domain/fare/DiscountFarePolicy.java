package nextstep.line.domain.fare;

import nextstep.auth.principal.UserPrincipal;
import nextstep.member.domain.Member;

public interface DiscountFarePolicy {

    boolean isSupport(Member member);

    Integer getFare(Integer fare);

}
