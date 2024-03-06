package nextstep.subway.application.dto;

import nextstep.subway.domain.Station;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int duration;
    private int fare;

    public PathResponse(final List<Station> stations, final int distance, final int duration, final int fare) {
        this.stations = stations.stream().map(this::createStationResponse).collect(Collectors.toList());
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

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public int getFare() {
        return this.fare;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PathResponse that = (PathResponse) o;
        return distance == that.distance && duration == that.duration && fare == that.fare && Objects.equals(stations, that.stations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, duration, fare);
    }
}
