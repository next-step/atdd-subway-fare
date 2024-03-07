package nextstep.path;

import lombok.Getter;
import lombok.Setter;
import nextstep.line.section.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

@Getter
@Setter
public class SectionEdge extends DefaultWeightedEdge {
    private Section section;
    private int distance;
    private int duration;

    public void setEdge(Section section) {
        this.section = section;
        this.distance = section.getDistance();
        this.duration = section.getDuration();
    }
}
