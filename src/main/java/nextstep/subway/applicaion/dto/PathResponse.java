package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;

public class PathResponse {

    private static final int MINIMUM_DISTANCE = 10;
    private static final int MINIMUM_FARE = 1250;

    private static final IntUnaryOperator BETWEEN_10_AND_50 = d -> (int) Math.ceil(d / (double) 5) * 100;
    private static final IntUnaryOperator OVER_50 = d -> (int) Math.ceil(d / (double) 8) * 100;

    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = distance == 0 ? 0 : MINIMUM_FARE + calculateOverFare();
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();

        return new PathResponse(stations, distance, duration);
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

    private int calculateOverFare() {
        int over50 = Math.max(distance - 50, 0);
        int between10and50 = Math.max(distance - over50 - MINIMUM_DISTANCE, 0);

        return BETWEEN_10_AND_50.applyAsInt(between10and50) + OVER_50.applyAsInt(over50);
    }
}
