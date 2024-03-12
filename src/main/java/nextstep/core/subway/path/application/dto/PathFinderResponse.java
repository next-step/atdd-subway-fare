package nextstep.core.subway.path.application.dto;


import nextstep.core.subway.path.domain.PathFinderResult;
import nextstep.core.subway.station.domain.Station;

import java.util.List;

public class PathFinderResponse {
    private final List<Station> stations;
    private final int distance;
    private final int duration;
    private final int fare;

    public PathFinderResponse(PathFinderResult result, int fare) {
        this.stations = result.getStations();
        this.distance = result.getDistance();
        this.duration = result.getDuration();
        this.fare = fare;
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

    public int getFare() {
        return fare;
    }
}
