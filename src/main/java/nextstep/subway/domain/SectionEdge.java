package nextstep.subway.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionEdge extends DefaultWeightedEdge {
    private final Section section;

    private SectionEdge(Section section) {
        this.section = section;
    }

    public static SectionEdge valueOf(Section section) {
        return new SectionEdge(section);
    }

    public int getDistance() {
        return section.getDistance();
    }

    public int getDuration() {
        return section.getDuration();
    }

    public Section getSection() {
        return section;
    }
}
