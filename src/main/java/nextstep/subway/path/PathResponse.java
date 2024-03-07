package nextstep.subway.path;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    List<StationResponse> stations;

    int distance;

    int duration;

    public PathResponse(List<StationResponse> stations, int distance, int duration) {
        this.stations = stations;
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
