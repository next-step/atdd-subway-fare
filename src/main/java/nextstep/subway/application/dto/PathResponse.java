package nextstep.subway.application.dto;

import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private List<StationResponse> stations;

    private int distance;
    private int duration;
    private int fee;

    public PathResponse(final List<Station> stations, final int distance, final int duration) {
        this.stations = stations.stream().map(this::createStationResponse).collect(Collectors.toList());
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

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public int getFee() {
        return this.fee;
    }
}
