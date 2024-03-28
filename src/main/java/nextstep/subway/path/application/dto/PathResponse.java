package nextstep.subway.path.application.dto;

import nextstep.subway.station.domain.Station;

import java.util.List;

public class PathResponse {
    private final List<Station> stations;
    private final int distance;
    private final int duration;
    private final int fare;

    public PathResponse(List<Station> stations, int distance, int duration, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.duration = duration;
        this.fare = fare;
    }

    public List<Station> getStations() {
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
