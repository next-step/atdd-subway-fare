package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.FarePolicy;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private FareResponse fare;

    public PathResponse(List<StationResponse> stations, int distance) {
        this(stations, distance, 0);
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this(stations, distance, duration, new FareResponse());
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, FareResponse fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static PathResponse of(Path path, FarePolicy farePolicy) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int amount = path.calculateFare(farePolicy);
        return new PathResponse(stations, distance, duration, new FareResponse(amount));
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

    public FareResponse getFare() {
        return fare;
    }
}
