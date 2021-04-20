package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;
import java.util.Set;

public class PathResult {
    private Sections sections;
    private Stations stations;

    public PathResult(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
    }

    public List<Station> getStations() {
        return stations.getStations();
    }

    public int getTotalDistance() {
        return sections.getTotalDistance();
    }

    public int getTotalDuration() {
        return sections.getTotalDuration();
    }

    public Set<Line> getGoThroughLine() {
        return sections.getGoThroughLine();
    }
}
