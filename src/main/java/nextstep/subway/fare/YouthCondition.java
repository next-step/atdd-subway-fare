package nextstep.subway.fare;

import nextstep.subway.auth.domain.EmptyMember;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.domain.LoginMember;

public class YouthCondition implements DiscountCondition{

    @Override
    public boolean isSatisfiedBy(LoginMember loginMember) {
        if (isLoginMember(loginMember)) {
            return loginMember.getAge() > 6 && loginMember.getAge() < 13;
        }
        return false;
    }

    private boolean isLoginMember(LoginMember loginMember) {
        return !EmptyMember.class.isInstance(loginMember);
    }
}
