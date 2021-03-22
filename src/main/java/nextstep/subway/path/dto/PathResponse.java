package nextstep.subway.path.dto;

import nextstep.subway.path.domain.PathResult;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, int distance, int duration) {
        this.stations = StationResponse.listOf(stations);
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse of(PathResult pathResult) {
        List<Station> stations = pathResult.getStations();
        int distance = pathResult.getTotalDistance();
        int duration = pathResult.getTotalDuration();
        return new PathResponse(stations, distance, duration);
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
}
