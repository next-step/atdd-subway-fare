package nextstep.subway.domain.fareOption;

import nextstep.member.domain.Member;

public class FareDiscountChildrenOption implements FareDiscountOption {
    private final int DEDUCTIONS = 350;

    @Override
    public boolean isDiscountTarget(Member member) {
        return member.getAge() >= 6 && member.getAge() < 13;
    }

    @Override
    public int calculateFare(int fare) {
        return (int)((fare - DEDUCTIONS) * 0.5 + DEDUCTIONS);
    }
}
