package nextstep.path;

import nextstep.station.Station;
import nextstep.station.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int duration;

    public PathResponse(List<Station> stations, int distance, int duration) {
        this.stations = stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
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
}
