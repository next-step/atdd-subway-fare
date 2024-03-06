package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

public class PathResponse {

    private List<StationResponse> stations;
    private Set<LineResponse> lines;
    private int distance;
    private int duration;

    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<Station> stations, Set<Line> lines, int distance, int duration ) {
        this.stations = stations.stream().map(StationResponse::new).collect(Collectors.toList());
        this.lines = lines.stream().map(LineResponse::new).collect(Collectors.toSet());
        this.distance = distance;
        this.duration = duration;
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

    public Set<LineResponse> getLines() {
        return lines;
    }

    public void updateFare(int fare) {
        this.fare = fare;
    }
}
