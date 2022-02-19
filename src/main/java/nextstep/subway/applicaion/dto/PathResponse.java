package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.domain.map.Path;

@JsonInclude(Include.NON_NULL)
public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int duration;
    private final int totalCost;
    private final LocalDateTime arrivalTime;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int totalCost, LocalDateTime arrivalTime) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.totalCost = totalCost;
        this.arrivalTime = arrivalTime;
    }

    public static PathResponse of(Path path, int fare) {
        List<StationResponse> stations = path.getStations().stream()
                                             .map(StationResponse::of)
                                             .collect(Collectors.toList());
        int distance = path.extractDistance();
        int duration = path.extractDuration();
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

    public int getTotalCost() {
        return totalCost;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }
}
