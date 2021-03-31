package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private long fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration,long fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static PathResponse of(PathResult pathResult,long fare) {
        int distance = pathResult.getTotalDistance();
        int duration = pathResult.getTotalDuration();
        return new PathResponse(StationResponse.listOf(pathResult.getStations()), distance, duration,fare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public long getFare() {
        return fare;
    }

    public int getDistance() {
        return distance;
    }

    public int getDuration() {
        return duration;
    }
}
