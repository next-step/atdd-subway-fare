package nextstep.subway.domain;

import nextstep.subway.constant.FindPathType;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Section section;
    private EdgeWeight edgeWeight;

    private SectionEdge(Section section, EdgeWeight edgeWeight) {
        this.section = section;
        this.edgeWeight = edgeWeight;
    }

    public static SectionEdge of(Section section, FindPathType type) {
        return new SectionEdge(section, FindPathType.getEdgeWeight(type));
    }

    public Section getSection() {
        return section;
    }

    public int getEdgeWeight() {
        return edgeWeight.getEdgeWeight(section);
    }
}
