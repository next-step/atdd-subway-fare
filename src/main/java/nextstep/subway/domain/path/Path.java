package nextstep.subway.domain.path;

import lombok.Getter;
import nextstep.subway.domain.line.Sections;
import nextstep.subway.domain.station.Station;

import java.util.List;

@Getter
public class Path {
    private Sections sections;

    private Path(Sections sections) {
        this.sections = sections;
    }

    public static Path of(Sections sections) {
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
