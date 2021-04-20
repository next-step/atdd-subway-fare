package nextstep.subway.path.domain.policy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.enums.LineFarePolicy;

import java.util.NoSuchElementException;
import java.util.Set;

public class LineFarePolicyCalculator extends FarePolicyCalculator {

    private final Set<Line> goThroughLine;

    public LineFarePolicyCalculator(Set<Line> goThroughLine) {
        this.goThroughLine = goThroughLine;
    }

    @Override
    public int calculate(int total) {
        return total + getAdditionalFare();
    }

    private int getAdditionalFare() {
        int maxFare = goThroughLine
                .stream()
                .mapToInt(line -> LineFarePolicy.find(line.getId()))
                .max()
                .orElseThrow(NoSuchElementException::new);

        return maxFare;
    }
}
