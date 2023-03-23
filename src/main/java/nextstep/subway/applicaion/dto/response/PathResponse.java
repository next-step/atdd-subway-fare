package nextstep.subway.applicaion.dto.response;

import lombok.Getter;
import nextstep.subway.domain.path.Path;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int duration;
    private final int price;

    public PathResponse(final List<StationResponse> stations, final int distance, final int duration, final int price) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
    }

    public static PathResponse of(final Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int price = path.extractPrice();

        return new PathResponse(stations, distance, duration, price);
    }
}
