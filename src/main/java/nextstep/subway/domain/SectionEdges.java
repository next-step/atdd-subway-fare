package nextstep.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEdges {

    private final List<SectionEdge> sectionEdgeGroup;

    public SectionEdges(List<SectionEdge> sectionEdgeGroup) {
        this.sectionEdgeGroup = sectionEdgeGroup;
    }

    public int calculateDistance() {
        return sectionEdgeGroup.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public List<Line> getLines() {
        return sectionEdgeGroup.stream()
                .map(SectionEdge::getLine)
                .distinct()
                .collect(Collectors.toList());
    }
}
