package nextstep.line.domain.path;

import nextstep.station.domain.Station;

import java.util.List;

public class ShortPath {

    List<Station> stations;
    Integer distance;
    Integer duration;
    Integer fare;

    public ShortPath(List<Station> stations, Integer distance, Integer duration, Integer fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
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

    public Integer getFare() {
        return fare;
    }
}
