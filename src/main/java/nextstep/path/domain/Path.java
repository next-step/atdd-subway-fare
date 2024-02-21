package nextstep.path.domain;

import nextstep.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final int duration;

    public Path(final List<Station> stations, final int distance, final int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }


    public int getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }
}
