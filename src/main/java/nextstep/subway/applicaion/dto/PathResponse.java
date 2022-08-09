package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private static final int DEFAULT_FARE = 1250;
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

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
        int fare = DEFAULT_FARE;
        if (10 < distance) {
            fare += calculateOverFare(distance);
        }
        return new PathResponse(stations, distance, duration, fare);
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

    private static int calculateOverFare(int distance) {
        if (distance <= 50) {
            BigDecimal firstOverFare = getScale(distance - 10, 5);
            return firstOverFare.intValue() * 100;
        }

        BigDecimal secondOverFare = getScale(distance - 50, 8);
        return 800 + secondOverFare.intValue() * 100;
    }

    private static BigDecimal getScale(int distance, int x) {
        return BigDecimal.valueOf((distance - 1) / x + 1).setScale(0, RoundingMode.CEILING);
    }
}
