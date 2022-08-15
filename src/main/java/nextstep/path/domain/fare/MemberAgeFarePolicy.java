package nextstep.path.domain.fare;

import nextstep.member.domain.GuestMember;
import nextstep.member.domain.Member;

public class MemberAgeFarePolicy implements FarePolicy {
    private static final double CHILD_DISCOUNT_AMOUNT = 0.5d;
    private static final double TEEN_DISCOUNT_AMOUNT = 0.2d;

    private final Member member;

    public MemberAgeFarePolicy(Member member) {
        this.member = member;
    }

    @Override
    public int apply(int beforeFare) {
        if (member instanceof GuestMember) {
            return beforeFare;
        }

        if (member.isChild()) {
            return (int) (beforeFare - beforeFare * CHILD_DISCOUNT_AMOUNT);
        }

        if (member.isTeen()) {
            return (int) (beforeFare - beforeFare * TEEN_DISCOUNT_AMOUNT);
        }

        return beforeFare;
    }
}
