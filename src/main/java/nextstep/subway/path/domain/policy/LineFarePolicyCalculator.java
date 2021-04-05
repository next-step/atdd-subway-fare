package nextstep.subway.path.domain.policy;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.enums.LineFarePolicy;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

public class LineFarePolicyCalculator implements FarePolicyCalculator {

    private final Sections sections;

    public LineFarePolicyCalculator(Sections sections) {
        this.sections = sections;
    }

    @Override
    public int calculate() {
        return getAdditionalFare();
    }

    private int getAdditionalFare(){
        Set<Line> goThroughLine = sections.getSections()
                .stream()
                .map(section -> section.getLine())
                .collect(Collectors.toSet());

        int maxFare = goThroughLine
                .stream()
                .mapToInt(line -> LineFarePolicy.find(line.getId()))
                .max()
                .orElseThrow(NoSuchElementException::new);

        return maxFare;
    }
}
