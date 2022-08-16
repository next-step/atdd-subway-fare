package nextstep.path.domain.fare;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.member.domain.Member;

import java.util.List;

public class FareCalculator {
    private static final int START_FARE = 0;

    private final FarePolicy chain;

    // Base -> Distance -> LineExtra -> AgeDiscount -> END
    public FareCalculator(Member member, List<Line> lines, List<Section> pathSections, int distance) {
        FarePolicy nullFarePolicy = new NullFarePolicy();
        FarePolicy ageDiscountFarePolicy = new AgeDiscountFarePolicy(member, nullFarePolicy);
        FarePolicy lineExtraFarePolicy = new LineExtraFarePolicy(lines, pathSections, ageDiscountFarePolicy);
        FarePolicy distanceFarePolicy = new DistanceFarePolicy(distance, lineExtraFarePolicy);
        this.chain = new BaseFarePolicy(distanceFarePolicy);
    }

    public int calculate() {
        return chain.apply(START_FARE);
    }
}
