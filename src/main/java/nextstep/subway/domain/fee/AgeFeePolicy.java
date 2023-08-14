package nextstep.subway.domain.fee;

import nextstep.member.domain.Member;

public class AgeFeePolicy extends FeePolicy {

    // member 가 null 인 경우 100살
    private int age = 100;

    public AgeFeePolicy(Member member) {
        if (member != null) {
            age = member.getAge();
        }
    }

    @Override
    protected int calculateFee(int fee) {
        return AgeFeeType.getAgeFeeTypeByAge(age).getDiscountFee(fee);
    }
}
