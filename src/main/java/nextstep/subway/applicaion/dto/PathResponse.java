package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final static int defaultFare = 1250;

    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = defaultFare;
        fare = calculateOverFare(distance, fare);
        return new PathResponse(stations, distance, duration, fare);
    }

    private static int calculateOverFare(int distance, int fare) {
        if (distance > 10 && distance <= 50) {
            fare += (int) ((Math.ceil((distance - 11) / 5) + 1) * 100);
        }
        if (distance > 50) {
            fare += (int) ((Math.ceil((distance - 11) / 8) + 1) * 100);
        }
        return fare;
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

    public int getFare() {
        return fare;
    }
}
