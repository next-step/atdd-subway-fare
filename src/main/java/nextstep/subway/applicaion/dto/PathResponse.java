package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int duration;
    private final int totalCost;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int totalCost) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.totalCost = totalCost;
    }

    public static PathResponse of(Path path, int totalCost) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();

        return new PathResponse(stations, distance, duration, totalCost);
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

    public int getTotalCost() {
        return totalCost;
    }
}
