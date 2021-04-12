package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

import java.util.List;

public class PathResult {
    private final static int DEFAULT_FARE = 1_250;

    private Sections sections;
    private Stations stations;
    private int fare;

    public PathResult(Stations stations, Sections sections) {
        this.stations = stations;
        this.sections = sections;
        this.fare = DEFAULT_FARE + calculateOverFare(this.sections.getTotalDistance());
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

    public int getFare() {return this.fare; }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance-1)/5)+1)*100);
    }
}
