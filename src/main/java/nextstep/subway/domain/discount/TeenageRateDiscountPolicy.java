package nextstep.subway.domain.discount;

import nextstep.subway.domain.LoginUser;

public class TeenageRateDiscountPolicy {

    private static final int MIN_AGE = 13;
    private static final int MAX_AGE = 19;

    public boolean isTarget(final LoginUser user) {
        return MIN_AGE <= user.getAge()
                && user.getAge() < MAX_AGE;
    }
}
