package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int requiredTime;

    public PathResponse(List<StationResponse> stations, int distance, int requiredTime) {
        this.stations = stations;
        this.distance = distance;
        this.requiredTime = requiredTime;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int requiredTime = path.extractRequiredTime();

        return new PathResponse(stations, distance, requiredTime);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getRequiredTime() {
        return requiredTime;
    }
}
