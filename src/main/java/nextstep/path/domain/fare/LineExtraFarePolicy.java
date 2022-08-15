package nextstep.path.domain.fare;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;

import java.util.List;

public class LineExtraFarePolicy implements FarePolicy {
    private final List<Line> lines;
    private final List<Section> pathSections;

    public LineExtraFarePolicy(List<Line> lines, List<Section> pathSections) {
        this.lines = lines;
        this.pathSections = pathSections;
    }

    @Override
    public int apply(int beforeFare) {
        int extraFare = lines.stream()
                .filter(it -> it.containsAnyOf(pathSections))
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);

        return beforeFare + extraFare;
    }
}
