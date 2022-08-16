package nextstep.path.domain.fare2;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.member.domain.Member;

import java.util.List;

public class FareCalculator {

    private final FarePolicy firstPolicy;

    public FareCalculator(Member member, List<Line> lines, List<Section> pathSections, int distance) {
        FarePolicy nullFarePolicy = new NullFarePolicy();
        FarePolicy ageDiscountFarePolicy = new AgeDiscountFarePolicy(member, nullFarePolicy);
        FarePolicy lineExtraFarePolicy = new LineExtraFarePolicy(lines, pathSections, ageDiscountFarePolicy);
        FarePolicy distanceFarePolicy = new DistanceFarePolicy(distance, lineExtraFarePolicy);
        FarePolicy baseFarePolicy = new BaseFarePolicy(distanceFarePolicy);
        this.firstPolicy = baseFarePolicy;
    }

    public int calculate() {
        return firstPolicy.apply(0);
    }
}
