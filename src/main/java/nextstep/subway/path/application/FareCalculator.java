package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.policy.*;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class FareCalculator {

    public FareCalculator() {
    }

    private final int BASIC_FARE = 1250;

    public int getTotalFare(Set<Line> goThroughLine, int distance, int age) {
        FarePolicyCalculator distanceFarePolicyCalculator = new DistanceFarePolicyCalculator(distance);
        FarePolicyCalculator lineFarePolicyCalculator = new LineFarePolicyCalculator(goThroughLine);
        FarePolicyCalculator ageFarePolicyCalculator = new AgeFarePolicyCalculator(age);

        distanceFarePolicyCalculator.setNext(lineFarePolicyCalculator);
        lineFarePolicyCalculator.setNext(ageFarePolicyCalculator);

        distanceFarePolicyCalculator.support(BASIC_FARE);

        return ageFarePolicyCalculator.getTotal();
    }
}
