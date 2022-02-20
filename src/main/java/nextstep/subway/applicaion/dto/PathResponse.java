package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class PathResponse {

    private final List<StationResponse> stations = new ArrayList<>();
    private final int distance;
    private int duration;
    private int fare;

    public PathResponse(List<Station> stations, int distance, int duration, int fare) {
        for (Station station : stations) {
            this.stations.add(StationResponse.createStationResponse(station));
        }
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

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                '}';
    }
}
