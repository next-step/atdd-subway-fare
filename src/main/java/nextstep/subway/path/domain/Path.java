package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class Path {
    private final List<Station> stations;
    private final Long distance;
    private final Long duration;

    public Path(List<Station> stations,
                Long distance,
                Long duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public Path(List<Station> shortestPath,
                Double shortestValue,
                Long value,
                PathType type) {
        this.stations = shortestPath;
        Long distance = 0L;
        Long duration = 0L;
        if(type == PathType.DURATION) {
            distance = value;
            duration = Math.round(shortestValue);
        }

        if(type == PathType.DISTANCE) {
            distance = Math.round(shortestValue);
            duration = value;
        }
        this.distance = distance;
        this.duration = duration;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getDuration() {
        return this.duration;
    }
}
