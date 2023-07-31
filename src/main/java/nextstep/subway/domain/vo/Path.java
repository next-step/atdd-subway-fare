package nextstep.subway.domain.vo;

import nextstep.subway.domain.Station;

import java.util.List;


public class Path {

    private List<Station> stations;
    private long distance;
    private int duration;

    public Path(List<Station> stations, long distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Station> getStations() {
        return stations;
    }

    public long getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
