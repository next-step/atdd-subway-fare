package nextstep.subway.fare;

import nextstep.subway.members.member.domain.LoginMember;

public interface DiscountCondition {
    boolean isSatisfiedBy(LoginMember loginMember);
}
