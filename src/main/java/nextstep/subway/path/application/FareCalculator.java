package nextstep.subway.path.application;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.policy.AgeFarePolicyCalculator;
import nextstep.subway.path.domain.policy.DistanceFarePolicyCalculator;
import nextstep.subway.path.domain.policy.FarePolicyCalculator;
import nextstep.subway.path.domain.policy.LineFarePolicyCalculator;
import org.springframework.stereotype.Component;

@Component
public class FareCalculator {

    public FareCalculator() {
    }

    private final int BASIC_FARE = 1250;

    public int getTotalFare(Sections sections, int age) {
        int distance = sections.getTotalDistance();
        FarePolicyCalculator distanceFarePolicyCalculator = new DistanceFarePolicyCalculator(distance);
        FarePolicyCalculator lineFarePolicyCalculator = new LineFarePolicyCalculator(sections);
        AgeFarePolicyCalculator ageFarePolicyCalculator = new AgeFarePolicyCalculator(age);

        return (int)((BASIC_FARE
                + distanceFarePolicyCalculator.calculate()
                + lineFarePolicyCalculator.calculate()
                - ageFarePolicyCalculator.getAgeDeduction())
                * (1 - ageFarePolicyCalculator.getAgeDiscount()));
    }
}
