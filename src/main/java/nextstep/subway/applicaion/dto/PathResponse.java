package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import nextstep.subway.domain.PathType;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PathResponse {
    private static int DEFULT_INT = 0;
    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
    }

    public static PathResponse of(Path path) {
        List<StationResponse> stations = path.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        if (PathType.시간.equals(path.getPathType())) {
            int duration = path.extractDuration();
            return new PathResponse(stations, DEFULT_INT, duration);
        }

        int distance = path.extractDistance();
        return new PathResponse(stations, distance, DEFULT_INT);
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
}
