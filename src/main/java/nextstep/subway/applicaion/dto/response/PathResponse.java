package nextstep.subway.applicaion.dto.response;

import lombok.Getter;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int duration;

    public PathResponse(final List<StationResponse> stations, final int distance, final int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse of(final Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();

        return new PathResponse(stations, distance, duration);
    }
}
