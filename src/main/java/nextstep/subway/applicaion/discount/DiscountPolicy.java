package nextstep.subway.applicaion.discount;

import nextstep.subway.domain.LoginUser;

public interface DiscountPolicy {

    boolean isTarget(LoginUser user);

    int discount(int fare);

}
