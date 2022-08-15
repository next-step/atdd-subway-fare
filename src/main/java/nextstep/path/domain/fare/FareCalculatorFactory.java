package nextstep.path.domain.fare;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.member.domain.Member;

import java.util.List;

public class FareCalculatorFactory {

    private FareCalculatorFactory() {
    }

    public static FareCalculator create(int distance, List<Line> lines, List<Section> sections, Member member) {
        return new FareCalculator(List.of(
                new DistanceFarePolicy(distance),
                new LineExtraFarePolicy(lines, sections),
                new MemberAgeFarePolicy(member)
        ));
    }
}
