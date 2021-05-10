package nextstep.subway.path.domain;

import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.path.domain.enumeration.DiscountType;

public class FareDiscountPolicy implements FarePolicy {

    private DiscountType discountType;

    public FareDiscountPolicy(LoginMember loginMember) {

        DiscountType type = DiscountType.NONE;

        if (!loginMember.isAnonymous()) {
            type = DiscountType.typeFromAge(loginMember.getAge());
        }

        new FareDiscountPolicy(type);
    }

    private FareDiscountPolicy(DiscountType discountType) {
        this.discountType = discountType;
    }

    @Override
    public int calculate(int beforeCost) {
        return beforeCost - discountType.discount(beforeCost);
    }
}
