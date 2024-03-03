package nextstep.core.subway.pathFinder.domain.dto;


import nextstep.core.subway.station.domain.Station;

import java.util.List;

public class PathFinderResult {
    private final List<Station> stations;
    private final Integer distance;
    private final Integer duration;

    public PathFinderResult(List<Station> stations, Integer distance, Integer duration) {
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
