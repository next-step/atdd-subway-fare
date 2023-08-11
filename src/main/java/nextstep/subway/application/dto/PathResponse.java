package nextstep.subway.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.FareCalculator;
import nextstep.subway.domain.Path;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private long fare;

    public PathResponse(List<StationResponse> stations, int distance, int duration, long fare) {
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
        long fare = path.getFare();

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

    public long getFare() {
        return fare;
    }
}
