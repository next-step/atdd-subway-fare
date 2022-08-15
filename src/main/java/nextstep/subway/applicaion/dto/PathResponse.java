package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Fare;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineFare;
import nextstep.subway.domain.Path;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
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
        return new PathResponse(stations, distance, duration, fare(path, distance));
    }

    private static int fare(Path path, int distance) {
        Fare fare = new Fare(lineFare(path), distance);
        return fare.calculate();
    }

    private static int lineFare(Path path) {
        List<Line> lines = path.allLinesPassingBy();
        LineFare lineFare = new LineFare(lines);
        return lineFare.get();
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
