package nextstep.subway.domain.path;

import nextstep.subway.domain.entity.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class SectionWeightedEdge extends DefaultWeightedEdge {
    private Section section;

    public SectionWeightedEdge(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public int getDistance() {
        return section.getDistance();
    }

    public int getDuration() {
        return section.getDuration();
    }
}
