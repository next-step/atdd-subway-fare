package nextstep.path.application.dto;

import nextstep.path.domain.Path;
import nextstep.station.application.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private int distance;
    private int duration;
    private long fare;
    private List<StationResponse> stations;

    public PathResponse() {
    }

    public PathResponse(final int distance, final int duration, final long fare, final List<StationResponse> stations) {
        this.distance = distance;
        this.duration = duration;
        this.stations = stations;
        this.fare = fare;
    }

    public static PathResponse from(final Path path, final long fare) {
        return new PathResponse(
                path.getDistance(),
                path.getDuration(),
                fare,
                path.getStations().stream().map(StationResponse::from).collect(Collectors.toList())
        );
    }

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public long getFare() {
        return fare;
    }
}
