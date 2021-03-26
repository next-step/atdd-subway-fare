package nextstep.subway.path.dto;

import nextstep.subway.path.domain.Cost;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private long cost;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, long cost) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.cost = cost;
    }

    public static PathResponse of(PathResult pathResult, Cost cost) {
        int distance = pathResult.getTotalDistance();
        int duration = pathResult.getTotalDuration();
        return new PathResponse(StationResponse.listOf(pathResult.getStations()), distance, duration, cost.getCost());
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

    public long getCost() {
        return cost;
    }
}
