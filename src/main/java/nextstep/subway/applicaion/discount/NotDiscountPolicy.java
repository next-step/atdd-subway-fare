package nextstep.subway.applicaion.discount;

import nextstep.subway.domain.LoginUser;

public class NotDiscountPolicy implements DiscountPolicy {

    @Override
    public boolean isTarget(final LoginUser user) {
        return true;
    }

    @Override
    public int discount(final int fare) {
        return fare;
    }
}
