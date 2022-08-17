package nextstep.subway.util.fare;

import nextstep.member.domain.Member;
import nextstep.subway.domain.Path;

public class AgeFare extends FareChain {
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.8;
    private static final int DEFAULT_FARE = 1250;
    private static final int EXCLUDE_FARE = 350;

    @Override
    public int calculate(Path path, Member member) {
        int fare = DEFAULT_FARE + super.calculate(path, member);

        if (member.isChildren()) {
            return (int) ((fare - EXCLUDE_FARE) * CHILDREN_DISCOUNT_RATE + EXCLUDE_FARE);
        }

        if (member.isTeenage()) {
            return (int) ((fare - EXCLUDE_FARE) * TEENAGER_DISCOUNT_RATE + EXCLUDE_FARE);
        }

        return fare;
    }
}
