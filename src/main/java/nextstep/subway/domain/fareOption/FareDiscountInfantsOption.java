package nextstep.subway.domain.fareOption;

import nextstep.member.domain.Member;

public class FareDiscountInfantsOption implements FareDiscountOption{

    @Override
    public boolean isDiscountTarget(Member member) {
        return member.getAge() < 6;
    }

    @Override
    public int calculateFare(int fare) {
        return 0;
    }
}
