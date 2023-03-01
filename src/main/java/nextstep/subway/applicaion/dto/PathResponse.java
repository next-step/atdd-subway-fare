package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private String totalFare;

    public PathResponse(List<StationResponse> stations, int distance, int duration, String totalFare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.totalFare = totalFare;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int duration = path.extractDuration();
        int distance = path.extractDistance();
        String totalFare = path.extractTotalFare();
        return new PathResponse(stations, distance, duration, totalFare);
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

    public String getTotalFare() {
        return totalFare;
    }
}
