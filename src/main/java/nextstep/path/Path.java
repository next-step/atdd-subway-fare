package nextstep.path;

import nextstep.station.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private int distance;
    private int duration;

    public Path(List<Station> stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
