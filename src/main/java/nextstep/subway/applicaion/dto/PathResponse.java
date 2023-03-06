package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private FareResponse fare;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime destinationDate;

    public PathResponse(List<StationResponse> stations, int distance) {
        this(stations, distance, 0);
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this(stations, distance, duration, new FareResponse(), LocalDateTime.now());
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, LocalDateTime destinationDate) {
        this(stations, distance, duration, new FareResponse(), destinationDate);
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, FareResponse fare, LocalDateTime destinationDate) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.destinationDate = destinationDate;
    }

    public static PathResponse of(Path path, int fare, LocalDateTime arrivalDate) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        return new PathResponse(stations, distance, duration, new FareResponse(fare), arrivalDate);
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

    public LocalDateTime getDestinationDate() {
        return destinationDate;
    }
}
