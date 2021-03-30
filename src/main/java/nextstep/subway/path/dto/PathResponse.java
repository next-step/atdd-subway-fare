package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static PathResponse of(PathResult pathResult, Fare fare) {
        int distance = pathResult.getTotalDistance();
        int duration = pathResult.getTotalDuration();
        int fareValue = fare.getFareValue();
        return new PathResponse(StationResponse.listOf(pathResult.getStations()), distance, duration, fareValue);
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
