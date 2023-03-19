package nextstep.subway.domain.path;

import nextstep.subway.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    public static SectionEdge of(final Section section) {
        return new SectionEdge(section);
    }

    public SectionEdge(final Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }
}
