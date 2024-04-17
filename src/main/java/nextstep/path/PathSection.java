package nextstep.path;

import nextstep.line.Line;
import nextstep.section.Section;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathSection extends DefaultWeightedEdge {

    private Line line;
    private Section section;

    public PathSection(Line line, Section section) {
        this.line = line;
        this.section = section;

    }

    public int getDuration() {
        return section.getDuration();
    }

    public int getDistance() {
        return section.getDistance();
    }

    public Line getLine() {
        return this.line;
    }
}
