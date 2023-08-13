package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Price;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int price;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int price) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.price = price;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int price = Price.calculate(distance).getPrice();
        return new PathResponse(stations, distance, duration, price);
    }
}
