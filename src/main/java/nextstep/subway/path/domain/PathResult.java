package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;

public class PathResult {
    private Sections sections;
    private Stations stations;
    private Fare fare;

    public PathResult(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
        this.fare = new Fare(getTotalDistance());
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

    public int getTotalFare() {
        return fare.getTotalFare();
    }
}
