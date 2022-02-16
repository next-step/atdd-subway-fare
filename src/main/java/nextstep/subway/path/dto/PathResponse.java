package nextstep.subway.path.dto;

import lombok.Getter;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stations, path.extractDistance(), path.extractDuration());
    }
}
