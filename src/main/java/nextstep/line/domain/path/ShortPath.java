package nextstep.line.domain.path;

import nextstep.station.domain.Station;

import java.util.List;

public class ShortPath {

    List<Station> stations;
    Integer distance;
    Integer duration;

    public ShortPath(List<Station> stations, Integer distance, Integer duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }
}
