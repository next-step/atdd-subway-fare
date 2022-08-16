package nextstep.path.domain.fare2;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;

import java.util.List;

public class LineExtraFarePolicy extends FarePolicy {
    private final List<Line> lines;
    private final List<Section> pathSections;

    public LineExtraFarePolicy(List<Line> lines, List<Section> pathSections, FarePolicy nextPolicy) {
        super(nextPolicy);
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

        return nextPolicy.apply(beforeFare + extraFare);
    }
}
