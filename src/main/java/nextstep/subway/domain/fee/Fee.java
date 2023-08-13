package nextstep.subway.domain.fee;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Line;

import java.util.List;

public class Fee {
    public static final int FIXED_AMOUNT = 1250;
    private final FeePolicy feePolicy;

    public Fee(int distance, List<Line> lines, Member member) {
        feePolicy = DistanceFeeType.getDistanceFee(distance)
                .setNext(new LineFeePolicy(lines))
                .setNext(new AgeFeePolicy(member));
    }

    public int calculateFee() {
        return feePolicy.getFee(FIXED_AMOUNT);
    }
}
