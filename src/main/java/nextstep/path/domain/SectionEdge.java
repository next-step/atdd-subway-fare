package nextstep.path.domain;

import nextstep.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public static SectionEdge of(Section section) {
        return new SectionEdge(section);
    }

    private SectionEdge(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }
}
