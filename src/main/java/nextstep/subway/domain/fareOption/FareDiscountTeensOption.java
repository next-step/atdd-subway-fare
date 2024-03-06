package nextstep.subway.domain.fareOption;

import nextstep.member.domain.Member;

public class FareDiscountTeensOption implements  FareDiscountOption {

    private static final int DEDUCTIONS = 350;

    @Override
    public boolean isDiscountTarget(Member member) {
        return member.getAge() >= 13 && member.getAge() < 19;
    }

    @Override
    public int calculateFare(int fare) {
        return (int)((fare - DEDUCTIONS) * 0.8 + DEDUCTIONS);
    }
}
