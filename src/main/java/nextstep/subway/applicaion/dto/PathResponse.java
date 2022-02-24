package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponse {
    private final List<StationResponse> stations;
    private final int distance;
    private final int duration;
    private final int fare;
    private final int additionFare;

    public PathResponse(List<Station> stations, int distance, int duration, int fare, int additionFare) {
        this.stations = stations.stream()
                .map(StationResponse::createStationResponse)
                .collect(Collectors.toList());
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
        this.additionFare = additionFare;
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

    public int getAdditionFare() {
        return additionFare;
    }

    @Override
    public String toString() {
        return "PathResponse{" +
                "stations=" + stations +
                ", distance=" + distance +
                ", duration=" + duration +
                ", fare=" + fare +
                ", additionFare=" + additionFare +
                '}';
    }
}
