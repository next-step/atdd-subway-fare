package nextstep.path.domain;

import nextstep.line.domain.Line;
import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathSection extends DefaultWeightedEdge {

    private final Line line;
    private final Section section;

    private PathSection(final Line line, final Section section) {
        this.line = line;
        this.section = section;
    }

    public static PathSection from(final Line line, final Section section) {
        return new PathSection(line, section);
    }

    public Station getUpStation() {
        return section.getUpStation();
    }

    public Station getDownStation() {
        return section.getDownStation();
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
