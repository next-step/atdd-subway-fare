package nextstep.subway.fare;

import nextstep.subway.auth.domain.EmptyMember;
import nextstep.subway.members.member.domain.LoginMember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public int calculateDiscountAmount(int fare, LoginMember loginMember) {
        for (DiscountCondition each : conditions) {
            if (each.isSatisfiedBy(loginMember)) {
                return getDiscountAmount(fare);
            }
        }
        return fare;
    }

    protected abstract int getDiscountAmount(int fare);
}
