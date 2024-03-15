package nextstep.core.subway.path.domain;

import nextstep.core.subway.station.domain.Station;

import java.util.List;

public class PathFinderResult {

    private final List<Station> stations;

    private final int distance;

    private final int duration;

    private final List<Integer> additionalFares;

    public PathFinderResult(List<Station> stations, int distance, int duration, List<Integer> additionalFares) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.additionalFares = additionalFares;
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

    public List<Integer> getAdditionalFares() {
        return additionalFares;
    }
}
