package nextstep.subway.applicaion.discount;

import nextstep.member.domain.Member;

public class NotDiscountPolicy implements DiscountPolicy {

    @Override
    public boolean isTarget(final Member member) {
        return true;
    }

    @Override
    public int discount(final int fare) {
        return fare;
    }
}
