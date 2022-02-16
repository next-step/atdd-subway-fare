package nextstep.subway.path.domain;

import lombok.Getter;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.List;

@Getter
public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public static Path from(Sections sections) {
        return new Path(sections);
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractDuration() {
        return sections.totalDuration();
    }

    public List<Station> getStations() {
        return sections.getStations();
    }
}
