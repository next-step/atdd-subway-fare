package nextstep.path.domain.fare2;

import nextstep.member.domain.GuestMember;
import nextstep.member.domain.Member;

public class AgeDiscountFarePolicy extends FarePolicy {
    private static final double CHILD_DISCOUNT_AMOUNT = 0.5d;
    private static final double TEEN_DISCOUNT_AMOUNT = 0.2d;

    private final Member member;

    AgeDiscountFarePolicy(Member member, FarePolicy nextPolicy) {
        super(nextPolicy);
        this.member = member;
    }

    @Override
    public int apply(int beforeFare) {
        if (member instanceof GuestMember) {
            return nextPolicy.apply(beforeFare);
        }

        if (member.isChild()) {
            return nextPolicy.apply((int) (beforeFare - beforeFare * CHILD_DISCOUNT_AMOUNT));
        }

        if (member.isTeen()) {
            return nextPolicy.apply((int) (beforeFare - beforeFare * TEEN_DISCOUNT_AMOUNT));
        }

        return nextPolicy.apply(beforeFare);
    }
}
