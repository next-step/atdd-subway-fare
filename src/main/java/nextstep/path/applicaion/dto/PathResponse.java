package nextstep.path.applicaion.dto;

import nextstep.path.domain.Path;
import nextstep.station.application.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int duration;
    private final int fare;

    public PathResponse(List<StationResponse> stations, Path path) {
        this.stations = stations;
        this.distance = path.getDistance();
        this.duration = path.getDuration();
        this.fare = path.getFare();
    }

    public List<StationResponse> getStations() {
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
