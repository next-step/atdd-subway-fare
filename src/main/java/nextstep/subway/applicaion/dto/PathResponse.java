package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.Path;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int price;
    private final int distance;
    private final int duration;
    private final int fare;

    private PathResponse(final List<StationResponse> stations, final int price, final int distance, final int duration, final int fare) {
        this.stations = stations;
        this.price = price;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static PathResponse of(Path path, int age) {
        List<StationResponse> stations = path.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
        int price = path.maximumPrice();
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.extractFare(age);

        return new PathResponse(stations, price, distance, duration, fare);
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public int getFare() {
        return fare;
    }

}
