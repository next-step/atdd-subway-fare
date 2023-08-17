package nextstep.subway.domain;

import nextstep.subway.constant.FindPathType;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private Section section;

    private SectionEdge(Section section) {
        this.section = section;
    }

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }

    public Section getSection() {
        return section;
    }
}
