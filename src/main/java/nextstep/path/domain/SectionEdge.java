package nextstep.path.domain;

import nextstep.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public int getDuration() {
        return section.getDuration();
    }

    public int getDistance() {
        return section.getDistance();
    }
}
