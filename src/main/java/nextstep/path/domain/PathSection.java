package nextstep.path.domain;

import nextstep.line.domain.Section;
import nextstep.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;

public class PathSection extends DefaultWeightedEdge {

    private final Section section;

    private PathSection(final Section section) {
        this.section = section;
    }

    public static PathSection from(final Section section) {
        return new PathSection(section);
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
}
