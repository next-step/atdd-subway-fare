package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private final List<StationResponse> stations = new ArrayList<>();
    private final int distance;
    private int duration;

    public PathResponse(List<Station> stations, int distance) {
        for (Station station : stations) {
            this.stations.add(StationResponse.createStationResponse(station));
        }
        this.distance = distance;
    }

    public PathResponse(List<Station> stations, int distance, int duration) {
        for (Station station : stations) {
            this.stations.add(StationResponse.createStationResponse(station));
        }
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

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
