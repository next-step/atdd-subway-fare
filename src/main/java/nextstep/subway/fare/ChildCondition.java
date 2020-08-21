package nextstep.subway.fare;

import nextstep.subway.auth.domain.EmptyMember;
import nextstep.subway.maps.map.domain.SubwayPath;
import nextstep.subway.members.member.domain.LoginMember;

public class ChildCondition implements DiscountCondition{

    @Override
    public boolean isSatisfiedBy(LoginMember loginMember) {

            return loginMember.getAge() > 13 && loginMember.getAge() < 20;

    }
}
