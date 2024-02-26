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
        this(shortestPath, getDistance(shortestValue, value, type), getDuration(shortestValue, value, type));
    }

    private static Long getDistance(Double shortestValue,
                                    Long value,
                                    PathType type) {
        if (type == PathType.DISTANCE) {
            return Math.round(shortestValue);
        }

        return value;

    }

    private static Long getDuration(Double shortestValue,
                                    Long value,
                                    PathType type) {
        if (type == PathType.DURATION) {
            return Math.round(shortestValue);
        }

        return value;

    }

    public Long fare() {
        return FarePolicy.calculate(this.distance);
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
