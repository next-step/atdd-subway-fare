package nextstep.path;

import nextstep.station.Station;

import java.util.List;

public class Path {

    private List<Station> stations;
    private int distance;

    public Path(List<Station> stations, double distance) {
        this.stations = stations;
        this.distance = (int) Math.round(distance);
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }
}
