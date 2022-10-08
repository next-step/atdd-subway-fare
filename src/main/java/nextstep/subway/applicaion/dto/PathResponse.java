package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import nextstep.subway.domain.Path;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
    private LocalDateTime arrivalTime;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare) {
        this(stations, distance, duration, fare, null);
    }

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare, LocalDateTime arrivalTime) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.arrivalTime = arrivalTime;
    }

    public static PathResponse of(Path path, int fare) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stations, path.extractDistance(), path.extractDuration(), fare);
    }

    public static PathResponse of(Path path, Station startStation, int fare, LocalDateTime arrivalTime) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        if (!isStartStation(startStation, path.getStations())) {
            Collections.reverse(stations);
        }
        return new PathResponse(stations, path.extractDistance(), path.extractDuration(), fare, arrivalTime);
    }

    private static boolean isStartStation(Station startStation, List<Station> stations) {
        return stations.get(0).equals(startStation);
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

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}
