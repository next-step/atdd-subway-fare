package nextstep.path.domain;

import nextstep.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge  {
    private final Section section;
    private final int edgeWeight;

    public static SectionEdge of(Section section, int edgeWeight) {
        return new SectionEdge(section, edgeWeight);
    }

    private SectionEdge(Section section, int edgeWeight) {
        this.section = section;
        this.edgeWeight = edgeWeight;
    }

    public Section getSection() {
        return section;
    }

    public Long source() {
        return section.getUpStationId();
    }

    public Long target() {
        return section.getDownStationId();
    }

    public int weight() {
        return edgeWeight;
    }
}
