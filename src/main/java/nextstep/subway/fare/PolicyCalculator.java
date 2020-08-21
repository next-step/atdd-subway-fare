package nextstep.subway.fare;

import nextstep.subway.auth.domain.EmptyMember;
import nextstep.subway.members.member.domain.LoginMember;

public class PolicyCalculator {

    private LoginMember loginMember;
    private int fare;

    public PolicyCalculator(LoginMember loginMember) {
        this.loginMember = loginMember;
    }

    public int calulate(LoginMember loginMember) {

        if (loginMember.getAge() > 6 && loginMember.getAge() < 13) {
            ChildPolicy childPolicy = new ChildPolicy(new ChildCondition());
            fare = childPolicy.calculateDiscountAmount(fare, loginMember);
        }

        if (loginMember.getAge() > 13 && loginMember.getAge() < 20) {
            YouthPolicy youthPolicy = new YouthPolicy(new YouthCondition());
            fare = youthPolicy.calculateDiscountAmount(fare, loginMember);
        }
        return fare;
    }

}
