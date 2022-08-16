package nextstep.path.domain.fare;

import nextstep.member.domain.Member;

public class NonDiscountMember extends Member {
    private static class GuestMemberHolder {
        private static final NonDiscountMember INSTANCE = new NonDiscountMember();
    }

    private static final int NON_DISCOUNT_AGE = 1000;

    public static NonDiscountMember getInstance() {
        return GuestMemberHolder.INSTANCE;
    }

    public NonDiscountMember() {
        super(null, null, NON_DISCOUNT_AGE);
    }
}
