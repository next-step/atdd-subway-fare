package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathMinimumDurationResponse {
    private List<StationResponse> stations;
    private int duration;

    public PathMinimumDurationResponse(List<StationResponse> stations, int duration) {
        this.stations = stations;
        this.duration = duration;
    }

    public static PathMinimumDurationResponse of(Path path) {
        List<StationResponse> stations = path.getStations()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDuration();

        return new PathMinimumDurationResponse(stations, distance);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }
}
