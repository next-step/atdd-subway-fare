package nextstep.subway.maps.map.dto;

import nextstep.subway.maps.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int duration;
    private int distance;
    private int fare;
    private int extraFare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int duration, int distance, int fare) {
        this(stations, duration, distance, fare, 0);
    }

    public PathResponse(List<StationResponse> stations, int duration, int distance, int fare, int extraFare) {
        this.stations = stations;
        this.duration = duration;
        this.distance = distance;
        this.fare = fare;
        this.extraFare = extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDuration() {
        return duration;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
