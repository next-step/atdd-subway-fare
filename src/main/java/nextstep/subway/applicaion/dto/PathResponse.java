package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Path;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.support.entity.Formatters.DATE_TIME_PATH;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;
    private LocalDateTime arrivalTime;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare, LocalDateTime arrivalTime) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.arrivalTime = arrivalTime;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
        int fare = path.getFare();
        LocalDateTime arrivalTime = path.getArrivalTime();
        return new PathResponse(stations, distance, duration, fare, arrivalTime);
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

    public String getArrivalTime() {
        if (this.arrivalTime == null) {
            return "";
        }
        return arrivalTime.format(DATE_TIME_PATH);
    }
}
