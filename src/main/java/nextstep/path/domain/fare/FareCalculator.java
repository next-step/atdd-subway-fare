package nextstep.path.domain.fare;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;

import java.util.List;

public class FareCalculator {
    private static final int START_FARE = 0;

    private final FarePolicy chain;

    // Base -> DistanceExtra -> LineExtra -> AgeDiscount -> END
    public FareCalculator(int age, List<Line> lines, List<Section> pathSections, int distance) {
        FarePolicy nullFarePolicy = new NullFarePolicy();
        FarePolicy ageDiscountFarePolicy = new AgeDiscountFarePolicy(age, nullFarePolicy);
        FarePolicy lineExtraFarePolicy = new LineExtraFarePolicy(lines, pathSections, ageDiscountFarePolicy);
        FarePolicy distanceFarePolicy = new DistanceExtraFarePolicy(distance, lineExtraFarePolicy);
        this.chain = new BaseFarePolicy(distanceFarePolicy);
    }

    public int calculate() {
        return chain.apply(START_FARE);
    }
}
