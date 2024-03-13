package nextstep.subway.path;

import nextstep.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {

    List<StationResponse> stations;

    int distance;

    int duration;

    int fare;

    public PathResponse(List<StationResponse> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
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
